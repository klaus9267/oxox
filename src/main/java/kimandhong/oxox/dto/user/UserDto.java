package kimandhong.oxox.dto.user;

import kimandhong.oxox.domain.Nickname;
import kimandhong.oxox.domain.User;
import lombok.Builder;

@Builder
public record UserDto(
    Long id,
    String email,
    String nickname,
    Long sequence
) {
  public static UserDto from(final Nickname nickname) {
    final User user = nickname.getUser();
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(nickname.getName())
        .sequence(nickname.getSequence())
        .build();
  }

  public static UserDto from(final User user) {
    final Nickname nickname = user.getNickname();
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(nickname.getName())
        .sequence(nickname.getSequence())
        .build();
  }
}
