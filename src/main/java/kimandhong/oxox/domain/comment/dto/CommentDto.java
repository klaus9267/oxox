package kimandhong.oxox.domain.comment.dto;

import kimandhong.oxox.domain.comment.domain.Comment;
import kimandhong.oxox.domain.reaction.domain.Emoji;
import kimandhong.oxox.domain.reaction.domain.Reaction;
import kimandhong.oxox.domain.user.dto.UserDto;
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
    Map<Emoji, Integer> reactions,
    Emoji myReaction
) {

  public static List<CommentDto> from(final List<Comment> comments, final Long userId) {
    return comments.stream()
        .map(comment -> CommentDto.from(comment, userId))
        .toList();
  }

  public static List<CommentDto> from(final List<Comment> comments) {
    return comments.stream()
        .map(CommentDto::from)
        .toList();
  }

  public static CommentDto from(final Comment comment, final Long userId) {
    final Emoji emoji = comment.getOneToMany().getReactions().stream()
        .filter(reaction -> reaction.getUser().getId().equals(userId))
        .findFirst()
        .map(Reaction::getEmoji)
        .orElse(Emoji.NONE);

    return CommentDto.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .user(UserDto.from(comment.getUser()))
        .createAt(comment.getCreatedAt())
        .reactions(comment.getEmojiCounts())
        .myReaction(emoji)
        .build();
  }

  public static CommentDto from(final Comment comment) {
    return CommentDto.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .user(UserDto.from(comment.getUser()))
        .createAt(comment.getCreatedAt())
        .reactions(comment.getEmojiCounts())
        .build();
  }
}
