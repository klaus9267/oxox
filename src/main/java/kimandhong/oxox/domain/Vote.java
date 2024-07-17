package kimandhong.oxox.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity(name = "votes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Vote extends TimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
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
