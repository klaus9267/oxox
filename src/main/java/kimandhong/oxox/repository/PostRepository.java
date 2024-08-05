package kimandhong.oxox.repository;

import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
  @Override
  @EntityGraph(attributePaths = {"user", "user.profile"})
  Optional<Post> findById(final Long id);

  Optional<Post> findByIdAndUserId(final Long postId, final Long userId);

  List<Post> findByIsDoneFalseAndCreatedAtBefore(LocalDateTime time);
}
