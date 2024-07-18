package kimandhong.oxox.repository;

import kimandhong.oxox.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  List<Profile> findAllByNickname(final String nickname);
}
