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
  SCARED("😨", "scared");

  private final String emoji;
  private final String name;
}
