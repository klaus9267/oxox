package kimandhong.oxox.repository;

import kimandhong.oxox.domain.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction,Long> {
  Optional<Reaction> findByCommentIdAndUserId(final Long commentId, final Long userId);
}
