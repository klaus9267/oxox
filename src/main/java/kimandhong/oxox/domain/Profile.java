package kimandhong.oxox.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kimandhong.oxox.dto.user.JoinDto;
import kimandhong.oxox.dto.user.SocialLoginDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "profiles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Profile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String image;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private Long sequence;

  @OneToOne(cascade = CascadeType.ALL)
  @JsonIgnore
  private User user;

  public static Profile from(final JoinDto joinDto, final User user, final Long sequence, final String profileImageUrl) {
    return Profile.builder()
        .nickname(joinDto.nickname())
        .user(user)
        .image(profileImageUrl)
        .sequence(sequence)
        .build();
  }

  public static Profile from(final SocialLoginDto loginDto, final Long sequence, final User user) {
    return Profile.builder()
        .nickname(loginDto.displayName())
        .sequence(sequence)
        .image(loginDto.photoUrl())
        .user(user)
        .build();
  }

  public void updateProfile(final String nickname, final Long sequence, final String image) {
    this.image = image;
    this.nickname = nickname;
    this.sequence = sequence;
  }

  public void updateSequence(final Long standardSequence) {
    if (this.sequence > standardSequence) {
      this.sequence--;
    }
  }
}

