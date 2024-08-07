package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.common.enums.S3path;
import kimandhong.oxox.controller.param.PostCondition;
import kimandhong.oxox.domain.*;
import kimandhong.oxox.dto.post.RequestPostDto;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.repository.PostRepository;
import kimandhong.oxox.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @DisplayName("게시글_조회_비로그인")
    public void login_false() throws Exception {
      Post post = initPost();

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
    @BeforeEach
    public void init() {
      initPosts();
    }

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
            int voteComparison = Integer.compare(b.getOneToMany().getVotes().size(), a.getOneToMany().getVotes().size());
            return voteComparison != 0 ? voteComparison : Long.compare(b.getId(), a.getId());
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
            int voteComparison = Integer.compare(b.getOneToMany().getVotes().size(), a.getOneToMany().getVotes().size());
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
            int emojiComparison = Integer.compare(getReactionCounts(b), getReactionCounts(a));
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

    private int getReactionCounts(Post post) {
      return post.getOneToMany().getComments().stream()
          .mapToInt(comment -> comment.getOneToMany().getReactions().size())
          .sum();

    }
  }

  @Nested
  @DisplayName("게시글_수정")
  class updatePost {
    @Test
    @DisplayName("썸네일_있음")
    public void thumbnail_exists() throws Exception {
      Post post = initPost();

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
      Post post = initPost();

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

    RequestPostDto requestPostDto = new RequestPostDto("title" + random.nextInt(9999), "content" + random.nextInt(9999));
    Post post = Post.from(requestPostDto, user, null);
    return postRepository.save(post);
  }

  @Transactional
  private void initPosts() {
    Random random = new Random();
    List<User> users = new ArrayList<>();

    for (int i = 0; i < 100; i++) {
      JoinDto joinDto = new JoinDto("test email", null, "test nickname");
      User newUser = User.from(joinDto, "password", 1L, null);
      users.add(newUser);
    }
    users = userRepository.saveAll(users);

    // 게시글 추가
    int randomPostCount = 10;
    for (int i = 0; i < randomPostCount; i++) {
      RequestPostDto requestPostDto = new RequestPostDto("title" + random.nextInt(9999), "content" + random.nextInt(9999));
      Post post = Post.from(requestPostDto, user, null);

      // 댓글 추가
      int randomCommentCount = random.nextInt(1, 15);
      for (int j = 0; j < randomCommentCount; j++) {
        Comment comment = Comment.from("content" + random.nextInt(9999), user, post);
        post.getOneToMany().getComments().add(comment);

        // 리액션 추가
        int randomReactionCount = random.nextInt(1, 5);
        A:
        for (int k = 0; k < randomReactionCount; k++) {
          User randomUser = users.get(random.nextInt(users.size()));
          Emoji randomEmoji = Emoji.values()[random.nextInt(Emoji.values().length - 2)];
          for (Reaction reaction : comment.getOneToMany().getReactions()) {
            if (reaction.getUser().getId().equals(user.getId())) {
              continue A;
            }
          }
          Reaction newReaction = Reaction.from(randomEmoji, randomUser, comment);
          comment.getOneToMany().getReactions().add(newReaction);
          comment.incrementCount(randomEmoji);
        }
      }

      // 투표 추가
      int randomVoteCount = random.nextInt(5, 20);
      A:
      for (int q = 0; q < randomVoteCount; q++) {
        User randomUser = users.get(random.nextInt(users.size()));
        for (Vote vote : post.getOneToMany().getVotes()) {
          if (vote.getUser().getId().equals(randomUser.getId())) {
            continue A;
          }
        }
        Vote newVote = Vote.from(random.nextBoolean(), randomUser, post);
        post.getOneToMany().getVotes().add(newVote);
      }

      postRepository.save(post);
    }
  }
}