package kimandhong.oxox.controller;

import kimandhong.oxox.common.BaseTestConfiguration;
import kimandhong.oxox.application.s3.S3path;
import kimandhong.oxox.domain.user.dto.JoinDto;
import kimandhong.oxox.domain.user.dto.LoginDto;
import kimandhong.oxox.domain.user.dto.SocialLoginDto;
import kimandhong.oxox.application.s3.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserIntegrationTest extends BaseTestConfiguration {
  private static final String END_POINT = "/api/users/";
  @MockBean
  S3Service s3Service;

  @Test
  void join() throws Exception {
    JoinDto joinDto = new JoinDto("testtest@email.com", "test password", "test nickname");

    when(s3Service.uploadFile(any(MultipartFile.class), any(S3path.class))).thenReturn(null);

    mockMvc.perform(multipart(END_POINT + "join")
            .param("email", joinDto.email())
            .param("password", joinDto.password())
            .param("nickname", joinDto.nickname())
            .content(objectMapper.writeValueAsString(joinDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(("$.email")).value(joinDto.email()))
        .andExpect(jsonPath(("$.nickname")).value(joinDto.nickname()));
  }

  @Test
  void login() throws Exception {
    LoginDto loginDto = new LoginDto(user.getEmail(), "test password");
    mockMvc.perform(post(END_POINT + "login")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(("$.id")).value(user.getId()))
        .andExpect(jsonPath(("$.email")).value(user.getEmail()))
        .andExpect(jsonPath(("$.nickname")).value(user.getProfile().getNickname()))
        .andExpect(jsonPath(("$.profileImage")).value(user.getProfile().getImage()));
  }

  @Test
  void socialLogin() throws Exception {
    SocialLoginDto socialLoginDto = new SocialLoginDto("testSocial@email.com", "test name", "test photoUrl", "test uid");

    mockMvc.perform(post(END_POINT + "login/social")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(socialLoginDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(("$.email")).value(socialLoginDto.email()))
        .andExpect(jsonPath(("$.nickname")).value(socialLoginDto.displayName()))
        .andExpect(jsonPath(("$.profileImage")).value(socialLoginDto.photoUrl()));
  }
}