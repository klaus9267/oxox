package kimandhong.oxox.domain.profile;

import kimandhong.oxox.application.auth.SecurityUtil;
import kimandhong.oxox.application.bulk.BulkRepository;
import kimandhong.oxox.application.handler.error.CustomException;
import kimandhong.oxox.application.handler.error.ErrorCode;
import kimandhong.oxox.application.s3.S3Service;
import kimandhong.oxox.application.s3.S3path;
import kimandhong.oxox.domain.profile.dto.ProfileDto;
import kimandhong.oxox.domain.profile.repository.ProfileCustomRepository;
import kimandhong.oxox.domain.profile.repository.ProfileRepository;
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
  private final BulkRepository bulkRepository;
  private final SecurityUtil securityUtil;


  @Transactional
  public ProfileDto updateProfile(final String nickname, final MultipartFile image) {
    final Profile profile = profileRepository.findByUserId(securityUtil.getCustomUserId()).orElseThrow(ErrorCode.NOT_FOUND_USER);
    final String profileImage = image != null
        ? s3Service.changeFile(profile.getImage(), image, S3path.PROFILE)
        : profile.getImage();

    try {
      final List<Profile> existingProfiles = profileRepository.findAllByNickname(profile.getNickname());
      for (final Profile existingProfile : existingProfiles) {
        if (!existingProfile.getId().equals(profile.getId())) {
          existingProfile.updateSequence(profile.getSequence());
        }
      }
      bulkRepository.updateProfileSequences(existingProfiles);

      final Long sequence = profile.getNickname().equals(nickname)
          ? profile.getSequence()
          : profileCustomRepository.findMaxSequenceByNickname(nickname) + 1;

      profile.updateProfile(nickname, sequence, profileImage);
      return ProfileDto.from(profile);
    } catch (Exception e) {
      s3Service.deleteFile(profileImage);
      throw new CustomException(ErrorCode.S3, e);
    }
  }
}
