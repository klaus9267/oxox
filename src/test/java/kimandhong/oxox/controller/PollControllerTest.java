package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.dto.poll.CreatePollDto;
import kimandhong.oxox.service.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PollControllerTest extends AbstractTest {
  private final String END_POINT = "/api/polls";
  @MockBean
  S3Service s3Service;

  @Test
  public void createPoll() throws Exception {

    CreatePollDto createPollDto = new CreatePollDto("Test Title", "Test Content");
    MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image required".getBytes());
    MockMultipartFile createPollDtoPart = new MockMultipartFile("createPollDto", null, MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(createPollDto).getBytes());

    when(s3Service.uploadThumbnail(any(MultipartFile.class))).thenReturn("thumbnail url");

    mockMvc.perform(multipart(END_POINT)
            .file(thumbnail)
            .param("title", createPollDto.title())
            .param("content", createPollDto.content())
//            .file(createPollDtoPart)
            .header("Authorization", token))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.title").value(createPollDto.title()))
        .andExpect(jsonPath("$.content").value(createPollDto.content()))
        .andExpect(jsonPath("$.thumbnailUrl").value("thumbnail url"));
  }
}