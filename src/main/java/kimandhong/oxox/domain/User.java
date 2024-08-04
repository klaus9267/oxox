package kimandhong.oxox.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.SocialLoginDto;
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

  //todo -> 소셜 로그인 사용자가 일반 로그인 시 예외처리
  @Column(nullable = false)
  private String password;
  private String uid;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private Profile profile;

  @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonIgnore
  private final List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonIgnore
  private final List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonIgnore
  private final List<Vote> votes = new ArrayList<>();

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
