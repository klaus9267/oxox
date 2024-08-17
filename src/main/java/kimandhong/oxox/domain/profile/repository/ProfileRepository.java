package kimandhong.oxox.domain.profile.repository;

import kimandhong.oxox.domain.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  List<Profile> findAllByNickname(final String nickname);

  Optional<Profile> findByUserId(final Long userId);
}
