package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kimandhong.oxox.auth.JwtUtil;
import kimandhong.oxox.common.swagger.SwaggerCreated;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.dto.user.SocialLoginDto;
import kimandhong.oxox.dto.user.UserDto;
import kimandhong.oxox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "USER API")
public class UserController {
  private final UserService userService;
  private final JwtUtil jwtUtil;
  private static final String TOKEN = "X-Access-Token";

  @PostMapping(value = "join", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @SwaggerCreated(summary = "회원가입")
  public ResponseEntity<UserDto> join(@ParameterObject @Valid final JoinDto joinDto,
                                      @RequestPart(required = false) final MultipartFile profileImage) {
    final User user = userService.join(joinDto, profileImage);

    return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.from(user));
  }

  @PostMapping("login")
  @SwaggerCreated(summary = "로그인")
  public ResponseEntity<UserDto> login(@RequestBody @Valid final LoginDto loginDto) {
    final User user = userService.login(loginDto);
    final String token = jwtUtil.createAccessToken(user);

    return ResponseEntity.status(HttpStatus.OK)
        .header(TOKEN, token)
        .header("Access-Control-Expose-Headers", TOKEN)
        .body(UserDto.from(user));
  }

  @PostMapping("login/social")
  @SwaggerCreated(summary = "소셜로그인")
  public ResponseEntity<UserDto> socialLogin(@RequestBody @Valid final SocialLoginDto loginDto) {
    final User user = userService.socialLogin(loginDto);
    final String token = jwtUtil.createAccessToken(user);

    return ResponseEntity.status(HttpStatus.OK)
        .header(TOKEN, token)
        .header("Access-Control-Expose-Headers", TOKEN)
        .body(UserDto.from(user));
  }
}
