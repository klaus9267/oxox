package kimandhong.oxox.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kimandhong.oxox.domain.common.TimeEntity;
import kimandhong.oxox.domain.profile.Profile;
import kimandhong.oxox.domain.user.dto.JoinDto;
import kimandhong.oxox.domain.user.dto.SocialLoginDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  private String password;
  private String uid;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private Profile profile;

  @Embedded
  private final UserOneToMany oneToMany = new UserOneToMany();

  private User(final JoinDto joinDto, final String password, final Long sequence, final String profileImage) {
    this.email = joinDto.email();
    this.password = password;
    this.profile = Profile.from(joinDto, this, sequence, profileImage);
  }

  private User(final SocialLoginDto loginDto, final Long sequence) {
    this.email = loginDto.email();
    this.uid = loginDto.uid();
    this.profile = Profile.from(loginDto, sequence, this);
  }

  public static User from(final JoinDto joinDto, final String password, final Long sequence, final String profileImage) {
    return new User(joinDto, password, sequence, profileImage);
  }

  public static User from(final SocialLoginDto loginDto, final Long sequence) {
    return new User(loginDto, sequence);
  }
}
