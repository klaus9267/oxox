package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.common.enums.S3path;
import kimandhong.oxox.service.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends AbstractTest {
  private final String END_POINT = "/api/profiles";
  @MockBean
  S3Service s3Service;

  @Nested
  @DisplayName("프로필_수정")
  class updateProfile {
    @Test
    @DisplayName("사진있음")
    public void image_exist() throws Exception {
      String newNickname = "new test nickname";
      MockMultipartFile image = new MockMultipartFile("image", "test image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image required".getBytes());

      when(s3Service.uploadFile(any(MultipartFile.class), any(S3path.class))).thenReturn("test s3 image");
      when(s3Service.changeFile(anyString(), any(MultipartFile.class), any(S3path.class))).thenReturn("test s3 image");

      mockMvc.perform(multipart(HttpMethod.PATCH, END_POINT)
              .file(image)
              .param("nickname", newNickname)
              .header("Authorization", token)
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.nickname").value(newNickname));
    }

    @Test
    @DisplayName("사진없음")
    public void image_null() throws Exception {
      String newNickname = "new test nickname";

      when(s3Service.uploadFile(any(MultipartFile.class), any(S3path.class))).thenReturn("test s3 image");
      when(s3Service.changeFile(anyString(), any(MultipartFile.class), any(S3path.class))).thenReturn("test s3 image");

      mockMvc.perform(multipart(HttpMethod.PATCH, END_POINT)
              .param("nickname", newNickname)
              .header("Authorization", token)
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.nickname").value(newNickname));
    }
  }
}