package kimandhong.oxox.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kimandhong.oxox.dto.profile.UpdateProfileDto;
import kimandhong.oxox.dto.user.JoinDto;
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
  private String emoji;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private Long sequence;

  @OneToOne(cascade = CascadeType.ALL)
  @JsonIgnore
  private User user;

  public static Profile from(final JoinDto joinDto, final User user, final Long sequence) {
    return Profile.builder()
        .nickname(joinDto.nickname())
        .user(user)
        .emoji(joinDto.profileEmoji())
        .sequence(sequence)
        .build();
  }

  public void updateProfile(final UpdateProfileDto updateProfileDto, final Long sequence) {
    this.emoji = updateProfileDto.emoji();
    this.nickname = updateProfileDto.nickname();
    this.sequence = sequence;
  }
}

