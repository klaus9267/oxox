package kimandhong.oxox.domain;

import jakarta.persistence.*;
import kimandhong.oxox.dto.poll.CreatePollDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "polls")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Poll {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  private String thumbnail;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @OneToMany(mappedBy = "poll", orphanRemoval = true)
  private final List<Comment> comments = new ArrayList<>();

  public static Poll from(final CreatePollDto pollDto, final User user, final String thumbnailUrl) {
    return Poll.builder()
        .title(pollDto.title())
        .content(pollDto.content())
        .user(user)
        .thumbnail(thumbnailUrl)
        .build();
  }
}
