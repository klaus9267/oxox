package kimandhong.oxox.domain.reaction.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Emoji {
  SMILE("😄", "smile"),
  SHOCK("🫨", "shock"),
  HEART("❤️", "heart"),
  CRY("️😭", "cry"),
  ANGRY("️🤬", "angry"),
  NONE("none", "none");

  private final String value;
  private final String name;
}
