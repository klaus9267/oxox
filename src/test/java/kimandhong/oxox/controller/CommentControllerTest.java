package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.domain.Comment;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.dto.post.RequestPostDto;
import kimandhong.oxox.repository.CommentRepository;
import kimandhong.oxox.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends AbstractTest {
  private final String END_POINT = "/api/comments";
  @Autowired
  PostRepository postRepository;
  @Autowired
  CommentRepository commentRepository;

  @Test
  @DisplayName("댓글_생성")
  public void createComment() throws Exception {
    Post post = initPost();
    mockMvc.perform(post(END_POINT)
            .param("postId", post.getId().toString())
            .param("content", "test content")
            .header("Authorization", token))
        .andExpect(status().isCreated());

    Post foundPost = postRepository.findById(post.getId()).orElseThrow(RuntimeException::new);
    assertThat(foundPost.getComments().size()).isEqualTo(1);
  }

  @Test
  @DisplayName("댓글_수정")
  public void updateComment() throws Exception {
    Comment comment = initComment();

    String content = "new content";
    mockMvc.perform(patch(END_POINT)
            .param("commentId", comment.getId().toString())
            .param("content", content)
            .header("Authorization", token))
        .andExpect(status().isNoContent());

    Comment foundComment = commentRepository.findById(comment.getId()).orElseThrow(RuntimeException::new);
    assertThat(foundComment.getContent()).isEqualTo(content);
  }

  @Test
  @DisplayName("댓글_삭제")
  public void deleteComment() throws Exception {
    Comment comment = initComment();

    mockMvc.perform(delete(END_POINT + "/" + comment.getId())
            .header("Authorization", token))
        .andExpect(status().isNoContent());

    assertThatThrownBy(() -> commentRepository.findById(comment.getId()).orElseThrow(RuntimeException::new))
        .isInstanceOf(RuntimeException.class);
  }

  private Post initPost() {
    Random random = new Random();

    RequestPostDto requestPostDto = new RequestPostDto("title" + random.nextInt(9999), "content" + random.nextInt(9999));
    Post post = Post.from(requestPostDto, user, null);
    return postRepository.save(post);
  }

  private Comment initComment() {
    Post post = initPost();
    Comment comment = Comment.from("test content", user, post);
    post.getComments().add(comment);
    return postRepository.save(post).getComments().get(0);
  }
}