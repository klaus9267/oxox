package kimandhong.oxox.repository;

import kimandhong.oxox.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  List<Profile> findAllByNickname(final String nickname);

  Optional<Profile> findByUserId(final Long userId);
}
