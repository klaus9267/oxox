package kimandhong.oxox.controller.param;

import lombok.Getter;

@Getter
public enum PostCondition {
  // 최고의 반응글
  BEST_REACTION, HOT, CLOSE,

  // 목록 조회
  POPULARITY, DEFAULT,

  // 마이 페이지
  WRITER,
  JOIN
}
