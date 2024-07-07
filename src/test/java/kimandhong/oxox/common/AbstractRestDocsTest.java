package kimandhong.oxox.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import kimandhong.oxox.auth.JwtUtil;
import kimandhong.oxox.config.SecurityConfig;
import kimandhong.oxox.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@Import(SecurityConfig.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AbstractRestDocsTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  private JwtUtil jwtUtil;
  @MockBean
  private UserRepository userRepository;
}
