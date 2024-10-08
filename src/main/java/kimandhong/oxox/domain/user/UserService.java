package kimandhong.oxox.domain.user;

import kimandhong.oxox.application.handler.error.CustomException;
import kimandhong.oxox.application.handler.error.ErrorCode;
import kimandhong.oxox.application.s3.S3Service;
import kimandhong.oxox.application.s3.S3path;
import kimandhong.oxox.domain.profile.repository.ProfileCustomRepository;
import kimandhong.oxox.domain.user.domain.User;
import kimandhong.oxox.domain.user.dto.JoinDto;
import kimandhong.oxox.domain.user.dto.LoginDto;
import kimandhong.oxox.domain.user.dto.SocialLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final S3Service s3Service;
  private final ProfileCustomRepository profileCustomRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public User join(final JoinDto joinDto, final MultipartFile file) {
    userRepository.findByEmail(joinDto.email()).ifPresent(user -> {
      throw new CustomException(user.getUid() == null
          ? ErrorCode.CONFLICT_EMAIL
          : ErrorCode.CONFLICT_GOOGLE);
    });

    final String password = passwordEncoder.encode(joinDto.password());
    final Long sequence = profileCustomRepository.findMaxSequenceByNickname(joinDto.nickname()) + 1;

    final String profileImage = file != null ? s3Service.uploadFile(file, S3path.PROFILE) : null;

    final User user = User.from(joinDto, password, sequence, profileImage);

    return userRepository.save(user);
  }

  public User login(final LoginDto loginDto) {
    return userRepository.findByEmail(loginDto.email())
        .map(user -> {
          if (user.getPassword() == null) {
            throw new CustomException(ErrorCode.CONFLICT_GOOGLE);
          }
          if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST_LOGIN);
          }
          return user;
        })
        .orElseThrow(ErrorCode.BAD_REQUEST_LOGIN);
  }

  public User socialLogin(final SocialLoginDto loginDto) {
    return userRepository.findByEmail(loginDto.email())
        .map(user -> {
          if (user.getPassword() != null) {
            throw new CustomException(ErrorCode.NOT_SOCIAL_USER);
          }
          if (!user.getUid().equals(loginDto.uid())) {
            throw new CustomException(ErrorCode.INVALID_UID);
          }
          return user;
        })
        .orElseGet(() -> {
          final Long sequence = profileCustomRepository.findMaxSequenceByNickname(loginDto.displayName()) + 1;
          final User newUser = User.from(loginDto, sequence);
          return userRepository.save(newUser);
        });
  }
}
