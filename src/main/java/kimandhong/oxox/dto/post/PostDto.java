package kimandhong.oxox.dto.post;

import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.Vote;
import kimandhong.oxox.dto.user.UserDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record PostDto(
    Long id,
    String title,
    String content,
    String thumbnailUrl,
    LocalDateTime createAt,
    boolean isDone,
    UserDto user,
    int commentCount,
    Long agreeCount,
    Long disAgreeCount
) {
  public static PostDto from(final Post post) {
    final UserDto userDto = UserDto.from(post.getUser());
    Map<Boolean, Long> voteCounts = post.getVotes().stream()
        .collect(Collectors.partitioningBy(Vote::isYes, Collectors.counting()));

    final Long agreeCount = voteCounts.get(true);
    final Long disagreeCount = voteCounts.get(false);

    return PostDto.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .thumbnailUrl(post.getThumbnail())
        .createAt(post.getCreatedAt())
        .isDone(post.isDone())
        .user(userDto)
        .commentCount(post.getComments().size())
        .agreeCount(agreeCount)
        .disAgreeCount(disagreeCount)
        .build();
  }

  public static List<PostDto> from(final List<Post> posts) {
    return posts.stream()
        .map(PostDto::from)
        .toList();
  }
}
