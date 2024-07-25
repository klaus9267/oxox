package kimandhong.oxox.controller.param;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record CommentPaginationParam(
    @Parameter(example = "0", required = true)
    Integer page,
    @Parameter(example = "10")
    Integer size
) {
  public CommentPaginationParam(final Integer page, final Integer size) {
    this.page = Math.max(0, page);
    this.size = Math.max(10, size);
  }

  public Pageable toPageable() {
    return PageRequest.of(page, size);
  }
}
