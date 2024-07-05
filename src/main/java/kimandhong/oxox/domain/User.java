package kimandhong.oxox.domain;

import jakarta.persistence.*;
import kimandhong.oxox.dto.user.JoinDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;
  private String uid;

  @OneToOne(mappedBy = "user")
  private Nickname nickname;

  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private final List<Poll> polls = new ArrayList<>();

  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private final List<Comment> comments = new ArrayList<>();

  public static User from(final JoinDto joinDto, final String password) {
    return User.builder()
        .email(joinDto.email())
        .password(password)
        .build();
  }
}
