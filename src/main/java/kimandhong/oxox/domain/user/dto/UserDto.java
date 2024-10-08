package kimandhong.oxox.domain.user.dto;

import kimandhong.oxox.domain.profile.Profile;
import kimandhong.oxox.domain.user.domain.User;
import lombok.Builder;

@Builder
public record UserDto(
    Long id,
    String email,
    String profileImage,
    String nickname,
    Long sequence
) {

  public static UserDto from(final Profile profile) {
    final User user = profile.getUser();
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .profileImage(profile.getImage())
        .nickname(profile.getNickname())
        .sequence(profile.getSequence())
        .build();
  }

  public static UserDto from(final User user) {
    final Profile profile = user.getProfile();
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .profileImage(profile.getImage())
        .nickname(profile.getNickname())
        .sequence(profile.getSequence())
        .build();
  }
}
