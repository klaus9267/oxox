package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.common.enums.S3path;
import kimandhong.oxox.controller.param.PostCondition;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.post.RequestPostDto;
import kimandhong.oxox.repository.PostRepository;
import kimandhong.oxox.service.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

  @Nested
  @DisplayName("게시글_생성")
  class createPost {
    @Test
    @DisplayName("썸네일_없음")
    public void thumbnail_null() throws Exception {
      RequestPostDto requestPostDto = new RequestPostDto("Test Title", "Test Content");

      when(s3Service.uploadFile(any(MultipartFile.class), any(S3path.class))).thenReturn("thumbnail url");

      mockMvc.perform(multipart(END_POINT)
              .param("title", requestPostDto.title())
              .param("content", requestPostDto.content())
              .header("Authorization", token))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("썸네일_있음")
    public void thumbnail_exists() throws Exception {
      RequestPostDto requestPostDto = new RequestPostDto("Test Title", "Test Content");
      MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image required".getBytes());

      when(s3Service.uploadFile(any(MultipartFile.class), any(S3path.class))).thenReturn("thumbnail url");

      mockMvc.perform(multipart(END_POINT)
              .file(thumbnail)
              .param("title", requestPostDto.title())
              .param("content", requestPostDto.content())
              .header("Authorization", token))
          .andExpect(status().isCreated());
    }
  }


  @Nested
  @DisplayName("게시글_상세조회")
  class readPost {
    @Test
    @DisplayName("게시글_조회_로그인")
    public void login_true() throws Exception {
      Post post = postRepository.findTopByOrderByIdAsc().orElseThrow(RuntimeException::new);

      mockMvc.perform(get(END_POINT + "/" + post.getId())
              .header("Authorization", token))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(post.getId()))
          .andExpect(jsonPath("$.title").value(post.getTitle()))
          .andExpect(jsonPath("$.content").value(post.getContent()))
          .andExpect(jsonPath("$.thumbnailUrl").value(post.getThumbnail()));
    }

    @Test
    @DisplayName("게시글_조회_비로그인")
    public void login_false() throws Exception {
      Post post = postRepository.findTopByOrderByIdAsc().orElseThrow(RuntimeException::new);

      mockMvc.perform(get(END_POINT + "/" + post.getId()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(post.getId()))
          .andExpect(jsonPath("$.title").value(post.getTitle()))
          .andExpect(jsonPath("$.content").value(post.getContent()))
          .andExpect(jsonPath("$.thumbnailUrl").value(post.getThumbnail()));
    }
  }


  @Nested
  @DisplayName("게시글_목록조회")
  class readPosts {
    @Test
    @DisplayName("기본_조회(최신순)")
    public void none() throws Exception {
      List<Post> posts = postRepository.findAll()
          .stream()
          .filter(post -> post.getCreatedAt().isAfter(LocalDateTime.now().minusDays(1)))
          .sorted((a, b) -> b.getId().compareTo(a.getId()))
          .toList();

      ResultActions result = mockMvc.perform(get(END_POINT)
              .param("page", String.valueOf(0))
              .param("size", String.valueOf(10)))
          .andExpect(status().isOk());

      for (int i = 0; i < 10; i++) {
        result.andExpect(jsonPath("$.posts[" + i + "].id").value(posts.get(i).getId()));
      }
    }

    @Test
    @DisplayName("인기순")
    public void popularity() throws Exception {
      List<Post> posts = postRepository.findAll()
          .stream()
          .filter(post -> post.getCreatedAt().isAfter(LocalDateTime.now().minusDays(1)))
          .sorted((a, b) -> {
            int voteComparison = Integer.compare(b.getVotes().size(), a.getVotes().size());
            if (voteComparison != 0) {
              return voteComparison;
            } else {
              return Long.compare(b.getId(), a.getId());
            }
          })
          .toList();

      ResultActions result = mockMvc.perform(get(END_POINT)
              .param("page", String.valueOf(0))
              .param("size", String.valueOf(10))
              .param("condition", PostCondition.POPULARITY.toString())
          )
          .andExpect(status().isOk());

      for (int i = 0; i < 10; i++) {
        result.andExpect(jsonPath("$.posts[" + i + "].id").value(posts.get(i).getId()));
      }
    }

    @Test
    @DisplayName("1시간 내 투표 많은 게시글")
    public void hot() throws Exception {
      List<Post> posts = postRepository.findAll()
          .stream()
          .filter(post -> post.getCreatedAt().isAfter(LocalDateTime.now().minusHours(1)))
          .sorted((a, b) -> {
            int voteComparison = Integer.compare(b.getVotes().size(), a.getVotes().size());
            if (voteComparison != 0) {
              return voteComparison;
            } else {
              return Long.compare(b.getId(), a.getId());
            }
          })
          .toList();

      ResultActions result = mockMvc.perform(get(END_POINT)
              .param("page", String.valueOf(0))
              .param("size", String.valueOf(10))
              .param("condition", PostCondition.HOT.toString())
          )
          .andExpect(status().isOk());

      for (int i = 0; i < 10; i++) {
        result.andExpect(jsonPath("$.posts[" + i + "].id").value(posts.get(i).getId()));
      }
    }

    @Test
    @DisplayName("반응 많은 게시글")
    public void bestReaction() throws Exception {
      List<Post> posts = postRepository.findAll()
          .stream()
          .filter(post -> post.getCreatedAt().isAfter(LocalDateTime.now().minusDays(1)))
          .sorted((a, b) -> {
            int emojiComparison = Integer.compare(getTotalEmojiCount(b), getTotalEmojiCount(a));
            if (emojiComparison != 0) {
              return emojiComparison;
            } else {
              return Long.compare(b.getId(), a.getId());
            }
          }).toList();

      ResultActions result = mockMvc.perform(get(END_POINT)
              .param("page", String.valueOf(0))
              .param("size", String.valueOf(10))
              .param("condition", PostCondition.BEST_REACTION.toString())
          )
          .andExpect(status().isOk());

      for (int i = 0; i < 10; i++) {
        result.andExpect(jsonPath("$.posts[" + i + "].id").value(posts.get(i).getId()));
      }
    }

    private int getTotalEmojiCount(Post post) {
      return post.getComments().stream()
          .flatMap(comment -> comment.getEmojiCounts().values().stream())
          .mapToInt(Integer::intValue)
          .sum();
    }
  }

  @Nested
  @DisplayName("게시글_수정")
  class updatePost {
    @Test
    @DisplayName("썸네일_있음")
    public void thumbnail_exists() throws Exception {
      Post post = postRepository.findTopByOrderByIdAsc().orElseThrow(RuntimeException::new);

      RequestPostDto requestPostDto = new RequestPostDto("Test Update Title", "Test Update Content");
      MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image required".getBytes());

      when(s3Service.changeFile(anyString(), any(MultipartFile.class), any(S3path.class))).thenReturn("thumbnail url");

      mockMvc.perform(multipart(HttpMethod.PATCH, END_POINT + "/" + post.getId())
              .file(thumbnail)
              .param("title", requestPostDto.title())
              .param("content", requestPostDto.content())
              .header("Authorization", token))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("썸네일_없음")
    public void thumbnail_null() throws Exception {
      Post post = postRepository.findTopByOrderByIdAsc().orElseThrow(RuntimeException::new);

      RequestPostDto requestPostDto = new RequestPostDto("Test Update Title", "Test Update Content");

      mockMvc.perform(multipart(HttpMethod.PATCH, END_POINT + "/" + post.getId())
              .param("title", requestPostDto.title())
              .param("content", requestPostDto.content())
              .header("Authorization", token))
          .andExpect(status().isNoContent());
    }
  }

  @Test
  @DisplayName("게시글_삭제")
  public void deletePost() throws Exception {
    Post post = initPost();

    mockMvc.perform(delete(END_POINT + "/" + post.getId())
            .header("Authorization", token))
        .andExpect(status().isNoContent());
  }

  private Post initPost() {
    Random random = new Random();
    User user = userRepository.findTopByOrderByIdAsc().orElseThrow(RuntimeException::new);

    RequestPostDto requestPostDto = new RequestPostDto("title" + random.nextInt(9999), "content" + random.nextInt(9999));
    Post post = Post.from(requestPostDto, user, null);
    return postRepository.save(post);
  }
}