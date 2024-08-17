package kimandhong.oxox.domain.reaction;

import kimandhong.oxox.application.auth.SecurityUtil;
import kimandhong.oxox.domain.comment.domain.Comment;
import kimandhong.oxox.domain.comment.CommentService;
import kimandhong.oxox.application.handler.error.ErrorCode;
import kimandhong.oxox.application.handler.error.exception.NotFoundException;
import kimandhong.oxox.domain.reaction.domain.Emoji;
import kimandhong.oxox.domain.reaction.domain.Reaction;
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
  public void react(final Long commentId, final Emoji emoji) {
    final Comment comment = commentService.findById(commentId);

    reactionRepository.findByCommentIdAndUserId(commentId, securityUtil.getCustomUserId())
        .ifPresentOrElse(reaction -> {
          if (emoji != null) {
            comment.decrementCount(reaction.getEmoji());
            reaction.updateEmoji(emoji);
            comment.incrementCount(emoji);
          } else {
            comment.decrementCount(reaction.getEmoji());
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
