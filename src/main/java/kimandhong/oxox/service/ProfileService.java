package kimandhong.oxox.service;

import kimandhong.oxox.auth.SecurityUtil;
import kimandhong.oxox.domain.Profile;
import kimandhong.oxox.dto.profile.ProfileDto;
import kimandhong.oxox.dto.profile.UpdateProfileDto;
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
    final Profile profile = profileRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 확인할 수 없습니다."));
    final Optional<Profile> optionalProfile = profileRepository.findFirstByNicknameOrderBySequenceDesc(updateProfileDto.nickname());

    if (optionalProfile.isEmpty()) {
      profile.updateProfile(updateProfileDto, 1L);
    } else if (optionalProfile.get().equals(profile)) {
      profile.updateProfile(updateProfileDto, profile.getSequence());
    } else if (!optionalProfile.get().equals(profile)) {
      profile.updateProfile(updateProfileDto, optionalProfile.get().getSequence() + 1);
    } else {
      throw new RuntimeException("잘못된 요청입니다.");
    }

    return ProfileDto.from(profile);
  }
}
