package kimandhong.oxox.common;

import jakarta.annotation.PostConstruct;
import kimandhong.oxox.auth.JwtUtil;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@TestConfiguration
@RequiredArgsConstructor
public class DataInitializer {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  public static String token;

  @PostConstruct
  @Transactional
  public void setContext() {
    JoinDto joinDto = new JoinDto("test@email.com", "test password", "test nickname", "test emoji");
    String password = passwordEncoder.encode(joinDto.password());
    User user = User.from(joinDto, password, 1L);
    User savedUser = userRepository.save(user);
    token = "Bearer " + jwtUtil.createAccessToken(savedUser);
  }
}
