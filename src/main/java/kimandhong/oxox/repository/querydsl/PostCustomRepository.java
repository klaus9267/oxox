package kimandhong.oxox.repository.querydsl;

import kimandhong.oxox.controller.param.SortType;
import kimandhong.oxox.domain.Post;

import java.util.List;

public interface PostCustomRepository {
  List<Post> findAllSorted(final SortType sortType);

  List<Post> findAllSortedWithUserId(final SortType sortType, final Long userId);
}
