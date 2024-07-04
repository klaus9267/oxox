package kimandhong.oxox.service;

import kimandhong.oxox.domain.Nickname;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.UserDto;
import kimandhong.oxox.repository.NicknameRepository;
import kimandhong.oxox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final NicknameRepository nicknameRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDto join(final JoinDto joinDto) {
    final String password = passwordEncoder.encode(joinDto.password());
    final User user = User.from(joinDto, password);

    final Nickname nickname = Nickname.from(joinDto.nickname(), user);
    final Nickname savedNickname = nicknameRepository.save(nickname);

    return UserDto.from(savedNickname);
  }

  public UserDto readUser(final Long userId) {
    final User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
    return UserDto.from(user);
  }
}
