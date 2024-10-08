package kimandhong.oxox.domain.vote;

import jakarta.persistence.*;
import kimandhong.oxox.domain.common.TimeEntity;
import kimandhong.oxox.domain.post.domain.Post;
import kimandhong.oxox.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "votes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Vote extends TimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "is_yes")
  private boolean isYes;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Post post;

  public static Vote from(final boolean isYes, final User user, final Post post) {
    return Vote.builder()
        .isYes(isYes)
        .user(user)
        .post(post)
        .build();
  }

  public void updateIsYes(final boolean isYes) {
    this.isYes = isYes;
  }
}
