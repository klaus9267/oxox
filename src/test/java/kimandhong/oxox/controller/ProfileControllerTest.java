package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.common.enums.S3path;
import kimandhong.oxox.service.S3Service;
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

  @Test
  public void updateProfile() throws Exception {
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
}