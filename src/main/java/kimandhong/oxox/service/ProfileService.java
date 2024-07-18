package kimandhong.oxox.service;

import kimandhong.oxox.auth.SecurityUtil;
import kimandhong.oxox.domain.Profile;
import kimandhong.oxox.dto.profile.ProfileDto;
import kimandhong.oxox.dto.profile.UpdateProfileDto;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import kimandhong.oxox.repository.ProfileRepository;
import kimandhong.oxox.repository.custom.ProfileCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileService {
  private final ProfileRepository profileRepository;
  private final ProfileCustomRepository profileCustomRepository;
  private final SecurityUtil securityUtil;


  @Transactional
  public ProfileDto updateProfile(final UpdateProfileDto updateProfileDto) {
    final Profile profile = profileRepository.findById(securityUtil.getCustomUserId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    final Long sequence = profile.getNickname().equals(updateProfileDto.nickname())
        ? profile.getSequence()
        : profileCustomRepository.findMaxSequenceByNickname(updateProfileDto.nickname()) + 1;

    final List<Profile> existingProfiles = profileRepository.findAllByNickname(profile.getNickname());
    for (final Profile existingProfile : existingProfiles) {
      if (existingProfile.getId().equals(profile.getId())) {
        continue;
      }
      existingProfile.updateSequence(profile.getSequence());
    }

    profile.updateProfile(updateProfileDto, sequence);
    return ProfileDto.from(profile);
  }
}
