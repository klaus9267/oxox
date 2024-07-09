package kimandhong.oxox.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import kimandhong.oxox.auth.JwtUtil;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AbstractRestDocsTest {
  @Autowired
  protected UserRepository userRepository;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  protected String token;

  @PostConstruct
  private void setUp() {
    User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
    token = "Bearer " + jwtUtil.createAccessToken(user);
  }
}
