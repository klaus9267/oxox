package kimandhong.oxox.domain;

import jakarta.persistence.*;
import kimandhong.oxox.domain.enums.ReactionEmoji;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "reactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Reaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated
  private ReactionEmoji reactionEmoji;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Comment comment;

  public static Reaction from(final ReactionEmoji emoji, final User user, final Comment comment) {
    return Reaction.builder()
        .reactionEmoji(emoji)
        .user(user)
        .comment(comment)
        .build();
  }

  public void updateEmoji(final ReactionEmoji emoji) {
    this.reactionEmoji = emoji;
  }
}
