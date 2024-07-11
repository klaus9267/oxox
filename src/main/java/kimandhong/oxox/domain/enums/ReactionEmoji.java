package kimandhong.oxox.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReactionEmoji {
  BELL("🔔"),
  COMPUTER("🖥️"),
  EYE("️👀"),
  SMILE("😊"),
  DICE("🎲"),
  HEART("❤️");

  private final String emoji;
}
