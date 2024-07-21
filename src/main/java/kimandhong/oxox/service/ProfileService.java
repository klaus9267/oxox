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
    final Profile profile = profileRepository.findById(securityUtil.getCustomUserId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    s3Service.deleteFile(profile.getImage());
    final String newImage = s3Service.uploadFile(image, S3path.PROFILE);

    try {
      final Long sequence = profile.getNickname().equals(nickname)
          ? profile.getSequence()
          : profileCustomRepository.findMaxSequenceByNickname(nickname) + 1;

      final List<Profile> existingProfiles = profileRepository.findAllByNickname(profile.getNickname());
      for (final Profile existingProfile : existingProfiles) {
        if (existingProfile.getId().equals(profile.getId())) {
          continue;
        }
        existingProfile.updateSequence(profile.getSequence());
      }

      profile.updateProfile(nickname, sequence, newImage);
      return ProfileDto.from(profile);
    } catch (Exception e) {
      s3Service.deleteFile(newImage);
      throw new RuntimeException(e.getMessage());
    }
  }
}
