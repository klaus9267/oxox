package kimandhong.oxox.service;

import kimandhong.oxox.auth.SecurityUtil;
import kimandhong.oxox.domain.Comment;
import kimandhong.oxox.domain.Reaction;
import kimandhong.oxox.domain.ReactionEmoji;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import kimandhong.oxox.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionService {
  private final ReactionRepository reactionRepository;
  private final CommentService commentService;
  private final SecurityUtil securityUtil;

  @Transactional
  public void react(final Long commentId, final ReactionEmoji emoji) {
    final Comment comment = commentService.findById(commentId);

    reactionRepository.findByCommentIdAndUserId(commentId, securityUtil.getCustomUserId())
        .ifPresentOrElse(reaction -> {
          if (emoji != null) {
            comment.decrementCount(reaction.getReactionEmoji());
            reaction.updateEmoji(emoji);
            comment.incrementCount(emoji);
          } else {
            comment.decrementCount(reaction.getReactionEmoji());
            reactionRepository.delete(reaction);
          }
        }, () -> {
          if (emoji != null) {
            final Reaction reaction = Reaction.from(emoji, securityUtil.getCurrentUser(), comment);
            reactionRepository.save(reaction);
            comment.incrementCount(emoji);
          } else {
            throw new NotFoundException(ErrorCode.NOT_FOUND_COMMENT);
          }
        });
  }
}
