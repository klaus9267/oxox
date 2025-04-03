package kimandhong.oxox.controller;

import kimandhong.oxox.application.bulk.BulkService;
import kimandhong.oxox.common.BaseTestConfiguration;
import kimandhong.oxox.domain.comment.domain.Comment;
import kimandhong.oxox.domain.comment.CommentRepository;
import kimandhong.oxox.domain.post.domain.Post;
import kimandhong.oxox.domain.post.repository.PostRepository;
import kimandhong.oxox.domain.reaction.domain.Reaction;
import kimandhong.oxox.domain.reaction.ReactionRepository;
import kimandhong.oxox.domain.user.domain.User;
import kimandhong.oxox.domain.user.UserRepository;
import kimandhong.oxox.domain.vote.Vote;
import kimandhong.oxox.domain.vote.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BulkIntegrationTest extends BaseTestConfiguration {
  private static final String END_POINT = "/api/bulks";

  @Autowired
  BulkService bulkService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  PostRepository postRepository;
  @Autowired
  CommentRepository commentRepository;
  @Autowired
  VoteRepository voteRepository;
  @Autowired
  ReactionRepository reactionRepository;

  @Test
  @DisplayName("유저_대규모_데이터_생성")
  void bulkInsertUsers() throws Exception {
    mockMvc.perform(post(END_POINT + "/users"))
        .andExpect(status().isOk());

    List<User> users = userRepository.findAll();
    assertThat(users).hasSizeGreaterThanOrEqualTo(1000);
  }

  @Test
  @DisplayName("게시글_대규모_데이터_생성")
  void bulkInsertPost() throws Exception {
    mockMvc.perform(post(END_POINT + "/posts"))
        .andExpect(status().isOk());

    List<Post> posts = postRepository.findAll();
    assertThat(posts).hasSize(1000);
  }

  @Test
  @DisplayName("댓글_대규모_데이터_생성")
  void bulkInsertComments() throws Exception {
    bulkService.bulkPosts();

    mockMvc.perform(post(END_POINT + "/comments"))
        .andExpect(status().isOk());

    List<Comment> comments = commentRepository.findAll();
    assertThat(comments).hasSize(1000);
  }

  @Test
  @DisplayName("투표_대규모_데이터_생성")
  void bulkInsertVotes() throws Exception {
    bulkService.bulkPosts();

    mockMvc.perform(post(END_POINT + "/votes"))
        .andExpect(status().isOk());

    List<Vote> votes = voteRepository.findAll();
    assertThat(votes).hasSize(10000);
  }

  @Test
  @DisplayName("리액션_대규모_데이터_생성")
  void bulkInsertReactions() throws Exception {
    bulkService.bulkPosts();
    bulkService.bulkComments();

    mockMvc.perform(post(END_POINT + "/reactions"))
        .andExpect(status().isOk());

    List<Reaction> reactions = reactionRepository.findAll();
    assertThat(reactions).hasSize(10000);
  }

  @Test
  @DisplayName("유저_대규모_데이터_삭제")
  void bulkDeleteUsers() throws Exception {
    mockMvc.perform(delete(END_POINT + "/users"))
        .andExpect(status().isOk());

    List<User> users = userRepository.findAll();
    assertThat(users).isEmpty();
  }

  @Test
  @DisplayName("게시글_대규모_데이터_삭제")
  void bulkDeletePost() throws Exception {
    mockMvc.perform(delete(END_POINT + "/posts"))
        .andExpect(status().isOk());

    List<Post> posts = postRepository.findAll();
    assertThat(posts).isEmpty();
  }

  @Test
  @DisplayName("댓글_대규모_데이터_삭제")
  void bulkDeleteComments() throws Exception {
    mockMvc.perform(delete(END_POINT + "/comments"))
        .andExpect(status().isOk());

    List<Comment> comments = commentRepository.findAll();
    assertThat(comments).isEmpty();
  }

  @Test
  @DisplayName("투표_대규모_데이터_삭제")
  void bulkDeleteVotes() throws Exception {
    mockMvc.perform(delete(END_POINT + "/votes"))
        .andExpect(status().isOk());

    List<Vote> votes = voteRepository.findAll();
    assertThat(votes).isEmpty();
  }

  @Test
  @DisplayName("리액션_대규모_데이터_삭제")
  void bulkDeleteReactions() throws Exception {
    mockMvc.perform(delete(END_POINT + "/reactions"))
        .andExpect(status().isOk());

    List<Reaction> reactions = reactionRepository.findAll();
    assertThat(reactions).isEmpty();
  }
}
