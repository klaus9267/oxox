package kimandhong.oxox.repository;

import kimandhong.oxox.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote,Long> {
  Optional<Vote> findByUserIdAndPostId(final Long userId, final Long postId);
}
