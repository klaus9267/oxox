package kimandhong.oxox.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Emoji {
  SMILE("😆", "smile"),
  LOVE("🥰", "love"),
  SHOCK("🫨", "shock"),
  ODD("🥴", "odd"),
  ANGRY("😡", "angry"),
  CRY("️😭", "cry"),
  POOP("️💩", "poop"),
  SCARED("😨", "scared"),
  NONE("none", "none");

  private final String value;
  private final String name;
}
