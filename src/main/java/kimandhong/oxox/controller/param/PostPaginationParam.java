package kimandhong.oxox.controller.param;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PostPaginationParam(
    @Parameter(example = "0", required = true)
    Integer page,
    @Parameter(example = "10")
    Integer size,
    @Parameter
    PostCondition postCondition
) {
  public PostPaginationParam(final Integer page, final Integer size, final PostCondition postCondition) {
    this.page = Math.max(0, page);
    this.size = Math.max(10, size);
    this.postCondition = postCondition;
  }

  public Pageable toPageable() {
    return PageRequest.of(page, size);
  }
}
