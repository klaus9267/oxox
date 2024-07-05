package kimandhong.oxox.controller;

import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.UserDto;
import kimandhong.oxox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("join")
  public ResponseEntity<UserDto> join(final @RequestBody JoinDto joinDto) {
    final UserDto userDto = userService.join(joinDto);
    return ResponseEntity.ok(userDto);
  }

  @GetMapping({"id"})
  public ResponseEntity<UserDto> readUser(@PathVariable final Long userId) {
    final UserDto userDto = userService.readUser(userId);
    return ResponseEntity.ok(userDto);
  }
}
