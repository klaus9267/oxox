package kimandhong.oxox.service;

import kimandhong.oxox.common.enums.S3path;
import kimandhong.oxox.domain.User;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.LoginDto;
import kimandhong.oxox.dto.user.SocialLoginDto;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.BadRequestException;
import kimandhong.oxox.handler.error.exception.ConflictException;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import kimandhong.oxox.repository.UserRepository;
import kimandhong.oxox.repository.custom.ProfileCustomRepository;
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
      throw new ConflictException(user.getUid() == null
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
            throw new BadRequestException(ErrorCode.CONFLICT_GOOGLE);
          }
          if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new NotFoundException(ErrorCode.BAD_REQUEST_LOGIN);
          }
          return user;
        })
        .orElseThrow(() -> new NotFoundException(ErrorCode.BAD_REQUEST_LOGIN));
  }

  public User socialLogin(final SocialLoginDto loginDto) {
    return userRepository.findByEmail(loginDto.email())
        .map(user -> {
          if (user.getPassword() != null) {
            throw new BadRequestException(ErrorCode.NOT_SOCIAL_USER);
          }
          if (!user.getUid().equals(loginDto.uid())) {
            throw new BadRequestException(ErrorCode.INVALID_UID);
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
