package kimandhong.oxox.domain;

import jakarta.persistence.*;
import kimandhong.oxox.dto.post.CreatePostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @OneToMany(mappedBy = "post", orphanRemoval = true)
  private final List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
  private final List<Vote> votes = new ArrayList<>();

  public static Post from(final CreatePostDto postDto, final User user, final String thumbnailUrl) {
    return Post.builder()
        .title(postDto.title())
        .content(postDto.content())
        .user(user)
        .thumbnail(thumbnailUrl)
        .build();
  }

  public static Post from(final String title, final String content, final User user, final String thumbnailUrl) {
    return Post.builder()
        .title(title)
        .content(content)
        .user(user)
        .thumbnail(thumbnailUrl)
        .build();
  }
}
