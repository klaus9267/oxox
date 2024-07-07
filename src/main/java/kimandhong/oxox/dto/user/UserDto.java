package kimandhong.oxox.dto.user;

import kimandhong.oxox.domain.Profile;
import kimandhong.oxox.domain.User;
import lombok.Builder;

@Builder
public record UserDto(
    Long id,
    String email,
    String profileEmoji,
    String nickname,
    Long sequence
) {
  public static UserDto from(final Profile profile) {
    final User user = profile.getUser();
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .profileEmoji(profile.getEmoji())
        .nickname(profile.getNickname())
        .sequence(profile.getSequence())
        .build();
  }

  public static UserDto from(final User user) {
    final Profile profile = user.getProfile();
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .profileEmoji(profile.getEmoji())
        .nickname(profile.getNickname())
        .sequence(profile.getSequence())
        .build();
  }
}
