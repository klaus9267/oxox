package kimandhong.oxox.dto.poll;

import kimandhong.oxox.domain.Poll;
import kimandhong.oxox.dto.user.UserDto;
import lombok.Builder;

@Builder
public record PollDto(
    Long id,
    String title,
    String content,
    String thumbnailUrl,
    UserDto user
) {
  public static PollDto from(final Poll poll) {
    final UserDto userDto = UserDto.from(poll.getUser());
    return PollDto.builder()
        .id(poll.getId())
        .title(poll.getTitle())
        .content(poll.getContent())
        .thumbnailUrl(poll.getThumbnail())
        .user(userDto)
        .build();
  }
}
