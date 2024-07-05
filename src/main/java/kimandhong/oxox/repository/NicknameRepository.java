package kimandhong.oxox.repository;

import kimandhong.oxox.domain.Nickname;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NicknameRepository extends JpaRepository<Nickname, Long> {
  List<Nickname> findByName(final String name);
}
