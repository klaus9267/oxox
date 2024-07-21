package kimandhong.oxox.dto.comment;

import kimandhong.oxox.domain.Comment;
import kimandhong.oxox.domain.ReactionEmoji;
import kimandhong.oxox.dto.user.UserDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
public record CommentDto(
    Long id,
    String content,
    UserDto user,
    LocalDateTime createAt,
    Map<ReactionEmoji, Integer> reactions
) {
  public static List<CommentDto> from(final List<Comment> comments) {
    return comments.stream()
        .map(comment -> CommentDto.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .user(UserDto.from(comment.getUser()))
            .createAt(comment.getCreatedAt())
            .reactions(comment.getEmojiCounts())
            .build())
        .toList();
  }
}
