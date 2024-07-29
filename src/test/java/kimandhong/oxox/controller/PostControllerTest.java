package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.common.enums.S3path;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    CreatePostDto createPostDto = new CreatePostDto("Test Title", "Test Content");
    MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image required".getBytes());

    when(s3Service.uploadFile(any(MultipartFile.class), any(S3path.class))).thenReturn("thumbnail url");

    mockMvc.perform(multipart(END_POINT)
            .file(thumbnail)
            .param("title", createPostDto.title())
            .param("content", createPostDto.content())
            .header("Authorization", token))
        .andExpect(status().isCreated());
  }

  @Test
  public void readPost() throws Exception {
    Post post = initPost();

    mockMvc.perform(get(END_POINT + "/" + post.getId())
            .header("Authorization", token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(post.getId()))
        .andExpect(jsonPath("$.title").value(post.getTitle()))
        .andExpect(jsonPath("$.content").value(post.getContent()))
        .andExpect(jsonPath("$.thumbnailUrl").value(post.getThumbnail()));
  }

  @Test
  public void deletePost() throws Exception {
    Post post = initPost();

    mockMvc.perform(delete(END_POINT + "/" + post.getId())
            .header("Authorization", token))
        .andExpect(status().isNoContent());
  }

  private Post initPost() {
    CreatePostDto createPostDto = new CreatePostDto("test title", "test content");
    Post post = Post.from(createPostDto, user, null);
    return postRepository.save(post);
  }
}