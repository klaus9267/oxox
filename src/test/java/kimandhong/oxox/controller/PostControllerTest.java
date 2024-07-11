package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.dto.post.CreatePostDto;
import kimandhong.oxox.repository.PostRepository;
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

class PostControllerTest extends AbstractTest {
  private final String END_POINT = "/api/posts";
  @MockBean
  S3Service s3Service;

  @Autowired
  PostRepository postRepository;

  @Test
  public void createPost() throws Exception {
    int id = postRepository.findAll().size() + 1;
    CreatePostDto createPostDto = new CreatePostDto("Test Title", "Test Content");
    String createPostDtoJson = objectMapper.writeValueAsString(createPostDto);
    MockMultipartFile createPostDtoPart = new MockMultipartFile("createPostDto", null, MediaType.APPLICATION_JSON_VALUE, createPostDtoJson.getBytes());
    MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image required".getBytes());

    when(s3Service.uploadThumbnail(any(MultipartFile.class))).thenReturn("thumbnail url");

    mockMvc.perform(multipart(END_POINT)
            .file(thumbnail)
            .file(createPostDtoPart)
            .header("Authorization", token))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value((long) id))
        .andExpect(jsonPath("$.title").value(createPostDto.title()))
        .andExpect(jsonPath("$.content").value(createPostDto.content()))
        .andExpect(jsonPath("$.thumbnailUrl").value("thumbnail url"));
  }

  @Test
  public void readPost() throws Exception {
    Post post = postRepository.findById(1L).orElseThrow(NoSuchElementException::new);

    mockMvc.perform(get(END_POINT + "/" + post.getId())
            .header("Authorization", token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(post.getId()))
        .andExpect(jsonPath("$.title").value(post.getTitle()))
        .andExpect(jsonPath("$.content").value(post.getContent()))
        .andExpect(jsonPath("$.thumbnailUrl").value(post.getThumbnail()));
  }
}