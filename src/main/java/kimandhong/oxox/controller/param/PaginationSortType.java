package kimandhong.oxox.controller.param;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum PaginationSortType {
  BEST_REACTION("best"),
  BEST_VOTE("best"),
  HOT("hot"),
  CLOSE("hot");

  private final String field;

  public Sort toSort(Sort.Direction direction) {
    return Sort.by(direction, field);
  }
}
