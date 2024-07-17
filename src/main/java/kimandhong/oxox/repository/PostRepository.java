package kimandhong.oxox.repository;

import kimandhong.oxox.domain.Post;
import kimandhong.oxox.repository.querydsl.PostCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
  Optional<Post> findByIdAndUserId(final Long postId, final Long userId);
}
