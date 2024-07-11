package kimandhong.oxox.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kimandhong.oxox.auth.JwtUtil;
import kimandhong.oxox.common.swagger.SwaggerCreated;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.dto.user.UserDto;
import kimandhong.oxox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "USER API")
public class UserController {
  private final UserService userService;
  private final JwtUtil jwtUtil;

  @PostMapping("join")
  @SwaggerCreated(summary = "회원가입")
  public ResponseEntity<UserDto> join(@RequestBody @Valid final JoinDto joinDto, final HttpServletResponse response) {
    final User user = userService.join(joinDto);
    final String token = jwtUtil.createAccessToken(user);

    return ResponseEntity.status(HttpStatus.CREATED).header("Authorization", token).body(UserDto.from(user));
  }

  @PostMapping("login")
  @SwaggerCreated(summary = "로그인")
  public ResponseEntity<UserDto> login(@RequestBody @Valid final LoginDto loginDto, final HttpServletResponse response) {
    final User user = userService.login(loginDto);
    final String token = jwtUtil.createAccessToken(user);

    return ResponseEntity.status(HttpStatus.OK).header("Authorization", token).body(UserDto.from(user));
  }


}
