package kimandhong.oxox.common;

import jakarta.annotation.PostConstruct;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TestConfiguration
@RequiredArgsConstructor
public class DataInitializer {
  private final UserRepository userRepository;

  @PostConstruct
  @Transactional
  public void setContext() {
    JoinDto joinDto = new JoinDto("test@email,com", "test password", "test nickname", "test emoji");
    User user = User.from(joinDto, "test password", 1L);
    userRepository.save(user);
  }
}
