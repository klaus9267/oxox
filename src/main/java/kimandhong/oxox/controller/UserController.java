package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kimandhong.oxox.auth.JwtUtil;
import kimandhong.oxox.auth.SecurityUtil;
import kimandhong.oxox.common.swagger.SwaggerCreated;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.dto.user.UserDto;
import kimandhong.oxox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "USER API")
public class UserController {
  private final UserService userService;
  private final SecurityUtil securityUtil;
  private final JwtUtil jwtUtil;

  @PostMapping("join")
  @SwaggerCreated(summary = "회원가입")
  public ResponseEntity<UserDto> join(@ParameterObject @Valid final JoinDto joinDto,
                                      @RequestPart(required = false) final MultipartFile profileImage) {
    final User user = userService.join(joinDto, profileImage);
    final String token = jwtUtil.createAccessToken(user);

    return ResponseEntity.status(HttpStatus.CREATED).header("Authorization", token).body(UserDto.from(user));
  }

  @PostMapping("login")
  @SwaggerCreated(summary = "로그인")
  public ResponseEntity<UserDto> login(@RequestBody @Valid final LoginDto loginDto) {
    final User user = userService.login(loginDto);
    final String token = jwtUtil.createAccessToken(user);

    return ResponseEntity.status(HttpStatus.OK).header("Authorization", token).body(UserDto.from(user));
  }
}
