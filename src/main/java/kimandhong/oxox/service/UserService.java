package kimandhong.oxox.service;

import kimandhong.oxox.domain.Profile;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.dto.user.UserDto;
import kimandhong.oxox.repository.ProfileRepository;
import kimandhong.oxox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ProfileRepository profileRepository;
  private final PasswordEncoder passwordEncoder;

  public User join(final JoinDto joinDto) {
    userRepository.findByEmail(joinDto.email())
        .ifPresent(i -> {
          throw new RuntimeException("중복된 email");
        });

    final String password = passwordEncoder.encode(joinDto.password());
    List<Profile> profiles = profileRepository.findByNickname(joinDto.nickname());
    final User user = User.from(joinDto, password, profiles.size());

    return userRepository.save(user);
  }

  public UserDto readUser(final Long userId) {
    final User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
    return UserDto.from(user);
  }

  public User login(final LoginDto loginDto) {
    User user = userRepository.findByEmail(loginDto.email()).orElseThrow(() -> new RuntimeException("없는 email"));
    if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
      throw new RuntimeException("잘못된 비밀번호");
    }
    return user;
  }
}
