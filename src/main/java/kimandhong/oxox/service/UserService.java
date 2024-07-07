package kimandhong.oxox.service;

import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.repository.ProfileRepository;
import kimandhong.oxox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ProfileRepository profileRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public User join(final JoinDto joinDto) {
    userRepository.findByEmail(joinDto.email())
        .ifPresent(i -> {
          throw new RuntimeException("중복된 email");
        });

    final String password = passwordEncoder.encode(joinDto.password());
    final Long sequence = profileRepository.findFirstByNicknameOrderBySequenceDesc(joinDto.nickname())
        .map(profile -> profile.getSequence() + 1).orElse(1L);

    final User user = User.from(joinDto, password, sequence);

    return userRepository.save(user);
  }

  public User login(final LoginDto loginDto) {
    User user = userRepository.findByEmail(loginDto.email()).orElseThrow(() -> new RuntimeException("없는 email"));
    if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
      throw new RuntimeException("잘못된 비밀번호");
    }
    return user;
  }
}
