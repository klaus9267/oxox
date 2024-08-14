package kimandhong.oxox.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import kimandhong.oxox.auth.JwtUtil;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
public class BaseTestConfiguration {
  @Autowired
  protected UserRepository userRepository;
  @Autowired
  JwtUtil jwtUtil;
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  PasswordEncoder passwordEncoder;
  protected String token;
  protected User user;

  @Value("${bulk.password}")
  private String password;

  @BeforeEach
  public void setUp() {
    user = initUser();
    token = "Bearer " + jwtUtil.createAccessToken(user);
  }

  private User initUser() {
    JoinDto joinDto = new JoinDto("new@email.com", null, "test nickname");
    String encodedPassword = passwordEncoder.encode(password);
    User newUser = User.from(joinDto, encodedPassword, 1L, null);
    return userRepository.save(newUser);
  }
}
