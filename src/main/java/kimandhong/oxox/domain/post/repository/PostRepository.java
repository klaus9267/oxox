package kimandhong.oxox.domain.post.repository;

import kimandhong.oxox.domain.post.domain.Post;
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


  List<Post> findTop5ByOrderByCreatedAtDesc();

  List<Post> findByIsDoneFalseAndCreatedAtBefore(LocalDateTime time);

}
