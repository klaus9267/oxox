package kimandhong.oxox.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class User extends TimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;
  private String uid;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private Profile profile;

  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private final List<Poll> polls = new ArrayList<>();

  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private final List<Comment> comments = new ArrayList<>();

  private User(final JoinDto joinDto, final String password, final Long sequence) {
    this.email = joinDto.email();
    this.password = password;
    this.profile = Profile.from(joinDto, this, sequence);
  }

  public static User from(final JoinDto joinDto, final String password, final Long sequence) {
    return new User(joinDto, password, sequence);
  }
}
