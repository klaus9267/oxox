package kimandhong.oxox.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import kimandhong.oxox.application.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@Disabled
@Import(SecurityConfig.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
public class AbstractRestDocsTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
}
