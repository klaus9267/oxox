package kimandhong.oxox.service;

import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.BadRequestException;
import kimandhong.oxox.handler.error.exception.ConflictException;
import kimandhong.oxox.handler.error.exception.NotFoundException;
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
    userRepository.findByEmail(joinDto.email()).ifPresent(user -> {
      if (user.getEmail().equals(joinDto.email())) {
        throw new ConflictException(user.getUid() == null ? ErrorCode.CONFLICT_EMAIL : ErrorCode.CONFLICT_GOOGLE);
      }
    });

    final String password = passwordEncoder.encode(joinDto.password());
    final Long sequence = profileRepository.findFirstByNicknameOrderBySequenceDesc(joinDto.nickname())
        .map(profile -> profile.getSequence() + 1).orElse(1L);

    final User user = User.from(joinDto, password, sequence);

    return userRepository.save(user);
  }

  public User login(final LoginDto loginDto) {
    User user = userRepository.findByEmail(loginDto.email()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
      throw new BadRequestException(ErrorCode.WRONG_PASSWORD);
    }
    return user;
  }
}
