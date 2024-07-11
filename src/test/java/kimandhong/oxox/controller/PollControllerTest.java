package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.domain.Poll;
import kimandhong.oxox.dto.poll.CreatePollDto;
import kimandhong.oxox.repository.PollRepository;
import kimandhong.oxox.service.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PollControllerTest extends AbstractTest {
  private final String END_POINT = "/api/polls";
  @MockBean
  S3Service s3Service;

  @Autowired
  PollRepository pollRepository;

  @Test
  public void createPoll() throws Exception {
    int id = pollRepository.findAll().size() + 1;
    CreatePollDto createPollDto = new CreatePollDto("Test Title", "Test Content");
    String createPollDtoJson = objectMapper.writeValueAsString(createPollDto);
    MockMultipartFile createPollDtoPart = new MockMultipartFile("createPollDto", null, MediaType.APPLICATION_JSON_VALUE, createPollDtoJson.getBytes());
    MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image required".getBytes());

    when(s3Service.uploadThumbnail(any(MultipartFile.class))).thenReturn("thumbnail url");

    mockMvc.perform(multipart(END_POINT)
            .file(thumbnail)
            .file(createPollDtoPart)
            .header("Authorization", token))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value((long) id))
        .andExpect(jsonPath("$.title").value(createPollDto.title()))
        .andExpect(jsonPath("$.content").value(createPollDto.content()))
        .andExpect(jsonPath("$.thumbnailUrl").value("thumbnail url"));
  }

  @Test
  public void readPoll() throws Exception {
    Poll poll = pollRepository.findById(1L).orElseThrow(NoSuchElementException::new);

    mockMvc.perform(get(END_POINT + "/" + poll.getId())
            .header("Authorization", token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(poll.getId()))
        .andExpect(jsonPath("$.title").value(poll.getTitle()))
        .andExpect(jsonPath("$.content").value(poll.getContent()))
        .andExpect(jsonPath("$.thumbnailUrl").value(poll.getThumbnail()));
  }
}