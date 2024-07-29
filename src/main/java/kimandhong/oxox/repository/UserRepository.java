package kimandhong.oxox.repository;

import kimandhong.oxox.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(final String email);

  @EntityGraph(attributePaths = "profile")
  List<User> findAll();
}
