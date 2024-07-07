package kimandhong.oxox.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kimandhong.oxox.auth.JwtUtil;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.dto.user.UserDto;
import kimandhong.oxox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final JwtUtil jwtUtil;

  @PostMapping("join")
  public ResponseEntity<UserDto> join(final @RequestBody @Valid JoinDto joinDto, final HttpServletResponse response) {
    final User user = userService.join(joinDto);
    final String token = jwtUtil.createAccessToken(user);
    response.setHeader("Authorization", token);

    return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.from(user));
  }

  @PostMapping("login")
  public ResponseEntity<UserDto> login(final @RequestBody LoginDto loginDto, final HttpServletResponse response) {
    final UserDto userDto = userService.login(loginDto);
//    final String token = jwtUtil.createAccessToken(user);
//    response.setHeader("Authorization", token);

    return ResponseEntity.ok(userDto);
  }

  @GetMapping({"id"})
  public ResponseEntity<UserDto> readUser(@PathVariable final Long userId) {
    final UserDto userDto = userService.readUser(userId);
    return ResponseEntity.ok(userDto);
  }
}
