package kimandhong.oxox.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "nicknames")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Nickname {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Long sequence;

  @OneToOne(cascade = CascadeType.ALL)
  private User user;

  public static Nickname from(final String name, final User user, final int sequence) {
    return Nickname.builder()
        .name(name)
        .user(user)
        .sequence((long) sequence + 1)
        .build();
  }
}

