package kimandhong.oxox.domain;

import jakarta.persistence.*;
import kimandhong.oxox.domain.enums.Emoji;
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
  private Emoji emoji;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Comment comment;
}
