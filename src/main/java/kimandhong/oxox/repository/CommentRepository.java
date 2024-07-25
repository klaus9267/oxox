package kimandhong.oxox.repository;

import kimandhong.oxox.domain.Comment;
import kimandhong.oxox.dto.comment.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
  Optional<Comment> findByIdAndUserId(final Long commentId, final Long userId);

  @EntityGraph(attributePaths = {"user", "user.profile"})
  List<Comment> findAllByPostId(final Long postId);

  Page<Comment> findAllByPostId(final Long postId, final Pageable pageable);
}
