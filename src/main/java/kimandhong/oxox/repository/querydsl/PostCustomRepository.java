package kimandhong.oxox.repository.querydsl;

import kimandhong.oxox.controller.param.PostPaginationParam;
import kimandhong.oxox.domain.Post;
import org.springframework.data.domain.PageImpl;

public interface PostCustomRepository {
  PageImpl<Post> findAllWithPagination(final PostPaginationParam param);

  PageImpl<Post> findAllWithPaginationAndUserId(final PostPaginationParam param, final Long userId);
}
