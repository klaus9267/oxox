package kimandhong.oxox.service;

import kimandhong.oxox.auth.SecurityUtil;
import kimandhong.oxox.domain.Profile;
import kimandhong.oxox.dto.profile.ProfileDto;
import kimandhong.oxox.dto.profile.UpdateProfileDto;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.BadRequestException;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import kimandhong.oxox.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileService {
  private final ProfileRepository profileRepository;
  private final SecurityUtil securityUtil;


  @Transactional
  public ProfileDto updateProfile(final UpdateProfileDto updateProfileDto) {
    final Long userId = securityUtil.getCustomUserId();
    final Profile profile = profileRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    final Optional<Profile> optionalProfile = profileRepository.findFirstByNicknameOrderBySequenceDesc(updateProfileDto.nickname());

    if (optionalProfile.isEmpty()) {
      profile.updateProfile(updateProfileDto, 1L);
    } else if (optionalProfile.get().equals(profile)) {
      profile.updateProfile(updateProfileDto, profile.getSequence());
    } else if (!optionalProfile.get().equals(profile)) {
      profile.updateProfile(updateProfileDto, optionalProfile.get().getSequence() + 1);
    } else {
      throw new BadRequestException(ErrorCode.BAD_REQUEST);
    }

    return ProfileDto.from(profile);
  }
}
