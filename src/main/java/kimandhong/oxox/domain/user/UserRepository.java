package kimandhong.oxox.domain.user;

import kimandhong.oxox.domain.user.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  @Cacheable(value = "users", key = "#id")
  Optional<User> findById(final Long id);

  Optional<User> findByEmail(final String email);

  Optional<User> findTopByOrderByIdAsc();

  @EntityGraph(attributePaths = "profile")
  List<User> findAll();

  Optional<User> findByUid(final String uid);
}
