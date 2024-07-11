package kimandhong.oxox.repository;

import kimandhong.oxox.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
  Optional<Comment> findByIdAndUserId(final Long commentId, final Long userId);

  List<Comment> findAllByPostId(final Long postId);
}
