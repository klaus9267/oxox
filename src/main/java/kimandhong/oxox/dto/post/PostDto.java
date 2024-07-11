package kimandhong.oxox.dto.post;

import kimandhong.oxox.domain.Post;
import kimandhong.oxox.dto.user.UserDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostDto(
    Long id,
    String title,
    String content,
    String thumbnailUrl,
    LocalDateTime createAt,
    UserDto user
) {
  public static PostDto from(final Post post) {
    final UserDto userDto = UserDto.from(post.getUser());
    return PostDto.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .thumbnailUrl(post.getThumbnail())
        .createAt(post.getCreateAt())
        .user(userDto)
        .build();
  }

  public static List<PostDto> from(final List<Post> posts) {
    return posts.stream()
        .map(PostDto::from)
        .toList();
  }
}
