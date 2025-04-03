package kimandhong.oxox.controller;

import kimandhong.oxox.common.BaseTestConfiguration;
import kimandhong.oxox.domain.comment.domain.Comment;
import kimandhong.oxox.domain.reaction.domain.Emoji;
import kimandhong.oxox.domain.post.domain.Post;
import kimandhong.oxox.domain.reaction.domain.Reaction;
import kimandhong.oxox.domain.post.dto.RequestPostDto;
import kimandhong.oxox.domain.comment.CommentRepository;
import kimandhong.oxox.domain.post.repository.PostRepository;
import kimandhong.oxox.domain.reaction.ReactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReactionIntegrationTest extends BaseTestConfiguration {
  private static final String END_POINT = "/api/reactions";
  @Autowired
  PostRepository postRepository;
  @Autowired
  CommentRepository commentRepository;
  @Autowired
  ReactionRepository reactionRepository;

  @Test
  @DisplayName("리액션_남기기")
  void react() throws Exception {
    for (Emoji emoji : Emoji.values()) {
      if (Emoji.NONE.equals(emoji)) {
        continue;
      }
      Comment comment = initComment();

      mockMvc.perform(post(END_POINT)
              .param("commentId", comment.getId().toString())
              .param("emoji", emoji.getName().toUpperCase())
              .header("Authorization", token))
          .andExpect(status().isOk());

      Reaction reaction = reactionRepository.findByCommentIdAndUserId(comment.getId(), user.getId()).orElseThrow(RuntimeException::new);
      assertThat(reaction.getEmoji()).isEqualTo(emoji);
    }
  }

  @Test
  @DisplayName("리액션_수정")
  void updateReact() throws Exception {
    for (Emoji emoji : Emoji.values()) {
      if (Emoji.NONE.getName().equals(emoji.getName())) {
        continue;
      }
      Reaction reaction = initReaction(emoji);
      for (Emoji newEmoji : Emoji.values()) {
        if (emoji.getName().equals(newEmoji.getName()) || Emoji.NONE.getName().equals(newEmoji.getName())) {
          continue;
        }
        mockMvc.perform(post(END_POINT)
                .param("commentId", reaction.getComment().getId().toString())
                .param("emoji", newEmoji.getName().toUpperCase())
                .header("Authorization", token))
            .andExpect(status().isOk());

        Reaction foundReaction = reactionRepository.findByCommentIdAndUserId(reaction.getComment().getId(), user.getId()).orElseThrow(RuntimeException::new);
        assertThat(foundReaction.getEmoji()).isEqualTo(newEmoji);
      }
    }
  }

  @Test
  @DisplayName("리액션_삭제")
  void deleteReact() throws Exception {
    for (Emoji emoji : Emoji.values()) {
      if (Emoji.NONE.getName().equals(emoji.getName())) {
        continue;
      }

      Reaction reaction = initReaction(emoji);

      mockMvc.perform(post(END_POINT)
              .param("commentId", reaction.getComment().getId().toString())
              .header("Authorization", token))
          .andExpect(status().isOk());

      Optional<Reaction> optionalReaction = reactionRepository.findById(reaction.getId());
      assertThatThrownBy(optionalReaction::get).isInstanceOf(RuntimeException.class);
    }
  }

  private Comment initComment() {
    Post post = initPost();
    Comment comment = Comment.from("test comment", user, post);
    return commentRepository.save(comment);
  }

  private Post initPost() {
    RequestPostDto postDto = new RequestPostDto("title", "content");
    Post post = Post.from(postDto, user, null);
    return postRepository.save(post);
  }

  private Reaction initReaction(Emoji emoji) {
    Comment comment = initComment();
    Reaction reaction = Reaction.from(emoji, user, comment);
    comment.incrementCount(emoji);
    return reactionRepository.save(reaction);
  }
}