package kimandhong.oxox.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Comment extends TimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Post post;

  @OneToMany(mappedBy = "comment", orphanRemoval = true)
  private final List<Reaction> reactions = new ArrayList<>();

  public static Comment from(final String content, final User user, final Post post) {
    return Comment.builder()
        .content(content)
        .user(user)
        .post(post)
        .build();
  }

  public void updateContent(final String content) {
    this.content = content;
  }
}
