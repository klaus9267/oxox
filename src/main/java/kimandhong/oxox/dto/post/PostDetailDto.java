package kimandhong.oxox.dto.post;

import kimandhong.oxox.domain.Comment;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.Vote;
import kimandhong.oxox.dto.comment.CommentDto;
import kimandhong.oxox.dto.user.UserDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record PostDetailDto(
    Long id,
    String title,
    String thumbnailUrl,
    String content,
    UserDto user,
    LocalDateTime createAt,
    boolean isDone,
    Long agreeCount,
    Long disAgreeCount,
    int commentCount,
    Boolean myVote,
    List<CommentDto> comments
) {
  public static PostDetailDto from(final Post post, final List<Comment> comments, final Long currentUserId) {
    final UserDto userDto = UserDto.from(post.getUser());
    final List<CommentDto> commentDtos = CommentDto.from(comments, currentUserId);
    Map<Boolean, Long> voteCounts = post.getVotes().stream()
        .collect(Collectors.partitioningBy(Vote::isYes, Collectors.counting()));

    Boolean isVoted = null;
    for (final Vote vote : post.getVotes()) {
      if (vote.getUser().getId().equals(currentUserId)) {
        isVoted = vote.isYes();
        break;
      }
    }
    final Long agreeCount = voteCounts.get(true);
    final Long disagreeCount = voteCounts.get(false);

    return PostDetailDto.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .user(userDto)
        .thumbnailUrl(post.getThumbnail())
        .createAt(post.getCreatedAt())
        .isDone(post.isDone())
        .agreeCount(agreeCount)
        .disAgreeCount(disagreeCount)
        .commentCount(comments.size())
        .comments(commentDtos)
        .myVote(isVoted)
        .build();
  }

  public static PostDetailDto from(final Post post, final List<Comment> comments) {
    final UserDto userDto = UserDto.from(post.getUser());
    final List<CommentDto> commentDtos = CommentDto.from(comments);
    Map<Boolean, Long> voteCounts = post.getVotes().stream()
        .collect(Collectors.partitioningBy(Vote::isYes, Collectors.counting()));

    final Long agreeCount = voteCounts.get(true);
    final Long disagreeCount = voteCounts.get(false);

    return PostDetailDto.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .user(userDto)
        .thumbnailUrl(post.getThumbnail())
        .createAt(post.getCreatedAt())
        .isDone(post.isDone())
        .agreeCount(agreeCount)
        .disAgreeCount(disagreeCount)
        .commentCount(comments.size())
        .comments(commentDtos)
        .build();
  }
}
