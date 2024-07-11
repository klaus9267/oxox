package kimandhong.oxox.dto.comment;

import kimandhong.oxox.domain.Comment;
import kimandhong.oxox.dto.user.UserDto;
import lombok.Builder;

import java.util.List;

@Builder
public record CommentDto(
    Long id,
    String content,
    UserDto user
// reactions will add
) {
  public static List<CommentDto> from(final List<Comment> comments) {
    return comments.stream()
        .map(comment -> CommentDto.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .user(UserDto.from(comment.getUser()))
            //todo: reactions will add
            .build())
        .toList();
  }
}
