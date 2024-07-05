package kimandhong.oxox.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import kimandhong.oxox.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@Import(SecurityConfig.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class AbstractRestDocsTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
}
