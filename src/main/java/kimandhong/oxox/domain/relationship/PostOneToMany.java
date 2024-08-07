package kimandhong.oxox.domain.relationship;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import kimandhong.oxox.domain.Comment;
import kimandhong.oxox.domain.Vote;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor
public class PostOneToMany {
  @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
  private final List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
  private final List<Vote> votes = new ArrayList<>();
}
