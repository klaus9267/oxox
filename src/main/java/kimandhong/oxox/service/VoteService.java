package kimandhong.oxox.service;

import kimandhong.oxox.auth.SecurityUtil;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.domain.Vote;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import kimandhong.oxox.repository.VoteRepository;
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
  public void vote(final Long postId, final boolean isYes) {
    final User user = securityUtil.getCurrentUser();
    final Post post = postService.findById(postId);

    voteRepository.findByUserIdAndPostId(user.getId(), postId)
        .ifPresentOrElse(vote -> vote.updateIsYes(isYes),
            () -> {
              final Vote vote = Vote.from(isYes, user, post);
              voteRepository.save(vote);
            }
        );
  }

  @Transactional
  public void deleteVote(final Long postId) {
    final User user = securityUtil.getCurrentUser();

    voteRepository.findByUserIdAndPostId(user.getId(), postId).ifPresentOrElse(
        voteRepository::delete,
        () -> {
          throw new NotFoundException(ErrorCode.NOT_FOUND_VOTE);
        }
    );
  }
}
