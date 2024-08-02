package kimandhong.oxox.controller;

import kimandhong.oxox.common.AbstractTest;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.Vote;
import kimandhong.oxox.dto.post.RequestPostDto;
import kimandhong.oxox.repository.PostRepository;
import kimandhong.oxox.repository.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractTest {
  private final String END_POINT = "/api/votes";

  @Autowired
  PostRepository postRepository;
  @Autowired
  VoteRepository voteRepository;

  @Test
  @DisplayName("투표하기_찬성")
  public void vote_true() throws Exception {
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
  public void vote_false() throws Exception {
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
  public void updateVote() throws Exception {
    Vote vote = initVote(true);

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
  public void deleteVote() throws Exception {
    Vote vote = initVote(true);

    mockMvc.perform(post(END_POINT)
            .param("postId", vote.getPost().getId().toString())
            .header("Authorization", token))
        .andExpect(status().isOk());

    assertThatThrownBy(() -> voteRepository.findById(vote.getId()).orElseThrow(RuntimeException::new))
        .isInstanceOf(RuntimeException.class);
  }

  private Post initPost() {
    RequestPostDto postDto = new RequestPostDto("title", "content");
    Post post = Post.from(postDto, user, null);
    return postRepository.save(post);
  }

  private Vote initVote(boolean isYes) {
    Post post = initPost();
    Vote vote = Vote.from(true, user, post);
    return voteRepository.save(vote);
  }
}