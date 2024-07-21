package kimandhong.oxox.dto.post;

import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class PostDto {
  private Long id;
  private String title;
  private String thumbnailUrl;
  private LocalDateTime createAt;
  private boolean isDone;
  private int commentCount;
  private Long agreeCount;
  private Long disAgreeCount;

  public static PostDto from(final Post post) {
    final Map<Boolean, Long> voteCounts = post.getVotes().stream()
        .collect(Collectors.partitioningBy(Vote::isYes, Collectors.counting()));

    return PostDto.builder()
        .id(post.getId())
        .title(post.getTitle())
        .thumbnailUrl(post.getThumbnail())
        .createAt(post.getCreatedAt())
        .isDone(post.isDone())
        .commentCount(post.getComments().size())
        .agreeCount(voteCounts.get(true))
        .disAgreeCount(voteCounts.get(false))
        .build();
  }

  public static List<PostDto> from(final List<Post> posts) {
    return posts.stream()
        .map(PostDto::from)
        .toList();
  }
}
