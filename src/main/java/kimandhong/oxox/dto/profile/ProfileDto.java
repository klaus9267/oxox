package kimandhong.oxox.dto.profile;

import kimandhong.oxox.domain.Profile;
import lombok.Builder;

@Builder
public record ProfileDto(
    Long id,
    String image,
    String nickname,
    Long sequence
) {
  public static ProfileDto from(final Profile profile) {
    return ProfileDto.builder()
        .id(profile.getId())
        .image(profile.getImage())
        .nickname(profile.getNickname())
        .sequence(profile.getSequence())
        .build();
  }
}
