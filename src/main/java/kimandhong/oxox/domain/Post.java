package kimandhong.oxox.domain;

import jakarta.persistence.*;
import kimandhong.oxox.dto.post.CreatePostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

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

  @Builder.Default
  @Column(name = "is_done")
  private boolean isDone = false;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @OneToMany(mappedBy = "post", orphanRemoval = true, fetch = FetchType.LAZY)
  @BatchSize(size = 1000)
  private final List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @BatchSize(size = 1000)
  private final List<Vote> votes = new ArrayList<>();

  public static Post from(final CreatePostDto postDto, final User user, final String thumbnailUrl) {
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
}
