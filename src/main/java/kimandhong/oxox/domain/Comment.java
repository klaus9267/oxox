package kimandhong.oxox.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kimandhong.oxox.domain.enums.ReactionEmoji;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Entity(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Comment extends TimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Post post;

  @OneToMany(mappedBy = "comment", orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<Reaction> reactions = new ArrayList<>();

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "reaction_counts", joinColumns = @JoinColumn(name = "comment_id"))
  @MapKeyEnumerated(EnumType.STRING)
  @MapKeyColumn(name = "emoji")
  private final Map<ReactionEmoji, Integer> emojiCounts = new EnumMap<>(ReactionEmoji.class);

  public static Comment from(final String content, final User user, final Post post) {
    return Comment.builder()
        .content(content)
        .user(user)
        .post(post)
        .build();
  }

  public void updateContent(final String content) {
    this.content = content;
  }

  public void incrementCount(final ReactionEmoji emoji) {
    emojiCounts.put(emoji, emojiCounts.getOrDefault(emoji, 0) + 1);
  }

  public void decrementCount(final ReactionEmoji emoji) {
    int count = emojiCounts.get(emoji);
    if (count < 2) {
      emojiCounts.remove(emoji);
    } else {
      emojiCounts.put(emoji, emojiCounts.getOrDefault(emoji, 0) - 1);
    }
  }
}
