package kimandhong.oxox.controller;

import kimandhong.oxox.common.BaseTestConfiguration;
import kimandhong.oxox.domain.post.domain.Post;
import kimandhong.oxox.domain.vote.Vote;
import kimandhong.oxox.domain.post.dto.RequestPostDto;
import kimandhong.oxox.domain.post.repository.PostRepository;
import kimandhong.oxox.domain.vote.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteIntegrationTest extends BaseTestConfiguration {
  private static final String END_POINT = "/api/votes";

  @Autowired
  PostRepository postRepository;
  @Autowired
  VoteRepository voteRepository;

  @Test
  @DisplayName("투표하기_찬성")
  void vote_true() throws Exception {
    Post post = initPost();

    mockMvc.perform(post(END_POINT)
            .param("postId", post.getId().toString())
            .param("isYes", "true")
            .header("Authorization", token))
        .andExpect(status().isOk());

    Vote vote = voteRepository.findByUserIdAndPostId(user.getId(), post.getId()).orElseThrow(RuntimeException::new);
    assertThat(vote.isYes()).isTrue();
  }

  @Test
  @DisplayName("투표하기_반대")
  void vote_false() throws Exception {
    Post post = initPost();

    mockMvc.perform(post(END_POINT)
            .param("postId", post.getId().toString())
            .param("isYes", "false")
            .header("Authorization", token))
        .andExpect(status().isOk());

    Vote vote = voteRepository.findByUserIdAndPostId(user.getId(), post.getId()).orElseThrow(RuntimeException::new);
    assertThat(vote.isYes()).isFalse();
  }

  @Test
  @DisplayName("투표수정")
  void updateVote() throws Exception {
    Vote vote = initVote();

    mockMvc.perform(post(END_POINT)
            .param("postId", vote.getPost().getId().toString())
            .param("isYes", "false")
            .header("Authorization", token))
        .andExpect(status().isOk());

    Vote foundVote = voteRepository.findById(vote.getId()).orElseThrow(RuntimeException::new);
    assertThat(foundVote.isYes()).isFalse();
  }

  @Test
  @DisplayName("투표취소")
  void deleteVote() throws Exception {
    Vote vote = initVote();

    mockMvc.perform(post(END_POINT)
            .param("postId", vote.getPost().getId().toString())
            .header("Authorization", token))
        .andExpect(status().isOk());

    Optional<Vote> optionalVote = voteRepository.findById(vote.getId());
    assertThatThrownBy(optionalVote::get).isInstanceOf(RuntimeException.class);
  }

  private Post initPost() {
    RequestPostDto postDto = new RequestPostDto("title", "content");
    Post post = Post.from(postDto, user, null);
    return postRepository.save(post);
  }

  private Vote initVote() {
    Post post = initPost();
    Vote vote = Vote.from(true, user, post);
    return voteRepository.save(vote);
  }
}