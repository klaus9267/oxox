package kimandhong.oxox.domain.vote;

import kimandhong.oxox.application.auth.SecurityUtil;
import kimandhong.oxox.domain.post.domain.Post;
import kimandhong.oxox.domain.post.PostService;
import kimandhong.oxox.domain.user.domain.User;
import kimandhong.oxox.application.handler.error.ErrorCode;
import kimandhong.oxox.application.handler.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {
  private final VoteRepository voteRepository;
  private final PostService postService;
  private final SecurityUtil securityUtil;

  @Transactional
  public void vote(final Long postId, final Boolean isYes) {
    final User user = securityUtil.getCurrentUser();
    final Post post = postService.findById(postId);

    voteRepository.findByUserIdAndPostId(user.getId(), postId)
        .ifPresentOrElse(vote -> {
          if (isYes != null) {
            vote.updateIsYes(isYes);
          } else {
            voteRepository.delete(vote);
          }
        }, () -> {
          if (isYes != null) {
            final Vote vote = Vote.from(isYes, user, post);
            voteRepository.save(vote);
          } else {
            throw new NotFoundException(ErrorCode.NOT_FOUND_VOTE);
          }
        });
  }
}
