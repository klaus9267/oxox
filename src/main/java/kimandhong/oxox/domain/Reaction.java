package kimandhong.oxox.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity(name = "reactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@BatchSize(size = 1000)
public class Reaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private Emoji emoji;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Comment comment;

  public static Reaction from(final Emoji emoji, final User user, final Comment comment) {
    return Reaction.builder()
        .emoji(emoji)
        .user(user)
        .comment(comment)
        .build();
  }

  public void updateEmoji(final Emoji emoji) {
    this.emoji = emoji;
  }
}
