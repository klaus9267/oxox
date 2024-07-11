package kimandhong.oxox.repository;

import kimandhong.oxox.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
//  @Query("""
//      SELECT p
//      FROM posts p
//           LEFT JOIN FETCH reactions r
//           LEFT JOIN FETCH votes v
//      WHERE u.id = :userId
//      """)
//  Page<Post> findAllByPagination(final Pageable pageable);
}
