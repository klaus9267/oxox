package kimandhong.oxox.domain.relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import kimandhong.oxox.domain.Comment;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.domain.Vote;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Embeddable
@Getter
@NoArgsConstructor
public class UserOneToMany {
  @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonIgnore
  private final List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonIgnore
  private final List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonIgnore
  private final List<Vote> votes = new ArrayList<>();
}
