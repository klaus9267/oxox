package kimandhong.oxox.domain.post.domain;

import jakarta.persistence.*;
import kimandhong.oxox.domain.common.TimeEntity;
import kimandhong.oxox.domain.user.domain.User;
import kimandhong.oxox.domain.post.dto.RequestPostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class Post extends TimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  private String thumbnail;

  @Builder.Default
  @Column(name = "is_done")
  private boolean isDone = false;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @Embedded
  private final PostOneToMany oneToMany = new PostOneToMany();

  public static Post from(final RequestPostDto postDto, final User user, final String thumbnailUrl) {
    return Post.builder()
        .title(postDto.title())
        .content(postDto.content())
        .user(user)
        .thumbnail(thumbnailUrl)
        .build();
  }

  public void done() {
    this.isDone = true;
  }

  public void updatePost(final RequestPostDto postDto, final String thumbnail) {
    this.title = postDto.title();
    this.content = postDto.content();
    this.thumbnail = thumbnail;
  }

  public void resetCreatedAt() {
    updateCreatedAt();
    this.isDone = false;
  }
}


