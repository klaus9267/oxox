package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.dto.profile.ProfileDto;
import org.junit.jupiter.api.Test;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends AbstractTest {
  private final String END_POINT = "/api/profiles";

  @Test
  public void updateProfile() throws Exception {
    ProfileDto profileDto = new ProfileDto(1L, "test image", "test nickname", 1L);

    mockMvc.perform(patch(END_POINT)
            .contentType(APPLICATION_JSON)
            .header("Authorization", token)
            )
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(profileDto)));
  }
}