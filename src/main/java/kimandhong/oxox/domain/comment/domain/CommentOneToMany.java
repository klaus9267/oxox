package kimandhong.oxox.domain.comment.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import kimandhong.oxox.domain.reaction.domain.Reaction;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor
public class CommentOneToMany {
  @OneToMany(mappedBy = "comment", orphanRemoval = true, cascade = CascadeType.ALL)
  private final List<Reaction> reactions = new ArrayList<>();
}
