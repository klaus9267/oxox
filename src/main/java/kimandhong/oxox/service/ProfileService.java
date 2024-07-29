package kimandhong.oxox.service;

import kimandhong.oxox.auth.SecurityUtil;
import kimandhong.oxox.common.enums.S3path;
import kimandhong.oxox.domain.Profile;
import kimandhong.oxox.dto.profile.ProfileDto;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import kimandhong.oxox.repository.ProfileRepository;
import kimandhong.oxox.repository.custom.ProfileCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileService {
  private final ProfileRepository profileRepository;
  private final S3Service s3Service;
  private final ProfileCustomRepository profileCustomRepository;
  private final SecurityUtil securityUtil;


  @Transactional
  public ProfileDto updateProfile(final String nickname, final MultipartFile image) {
    final Profile profile = profileRepository.findByUserId(securityUtil.getCustomUserId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    final String profileImage = image != null
        ? s3Service.changeFile(profile.getImage(), image, S3path.PROFILE)
        : profile.getImage();

    try {
      final Long sequence = profile.getNickname().equals(nickname)
          ? profile.getSequence()
          : profileCustomRepository.findMaxSequenceByNickname(nickname) + 1;

      final List<Profile> existingProfiles = profileRepository.findAllByNickname(profile.getNickname());
      for (final Profile existingProfile : existingProfiles) {
        if (!existingProfile.getId().equals(profile.getId())) {
          existingProfile.updateSequence(profile.getSequence());
        }
      }

      profile.updateProfile(nickname, sequence, profileImage);
      return ProfileDto.from(profile);
    } catch (Exception e) {
      s3Service.deleteFile(profileImage);
      throw new RuntimeException(e.getMessage());
    }
  }
}
