package kimandhong.oxox.repository.querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kimandhong.oxox.controller.param.PaginationSortType;
import kimandhong.oxox.controller.param.PostPaginationParam;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.BadRequestException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kimandhong.oxox.domain.QPost.post;
import static kimandhong.oxox.domain.QVote.vote;

@Repository
public class PostCustomRepositoryImpl implements PostCustomRepository {
  private final JPAQueryFactory jpaQueryFactory;

  public PostCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public PageImpl<Post> findAllWithPagination(PostPaginationParam param) {
    final Pageable pageable = param.toPageable();
    if (PaginationSortType.JOIN.equals(param.sortType()) || PaginationSortType.WRITER.equals(param.sortType())) {
      throw new BadRequestException(ErrorCode.WRONG_PARAMETER);
    }

    final JPAQuery<Post> query = jpaQueryFactory.selectFrom(post);
    final PaginationSortType sortType = param.sortType();

    if (PaginationSortType.POPULARITY.equals(sortType)) {
      query.orderBy(post.votes.size().asc());
    }
//    else if (PaginationSortType.JOIN.equals(sortType)) {
//
//    }

    List<Post> posts = jpaQueryFactory.selectFrom(post)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(posts, pageable, posts.size());
  }

  @Override
  public PageImpl<Post> findAllWithPaginationAndUserId(PostPaginationParam param, Long userId) {
    final Pageable pageable = param.toPageable();
    final PaginationSortType sortType = param.sortType();
    final JPAQuery<Post> query = jpaQueryFactory.selectFrom(post);

    if (PaginationSortType.WRITER.equals(sortType)) {
      query.where(post.user.id.eq(userId));
    } else if (PaginationSortType.JOIN.equals(sortType)) {
      query.leftJoin(post.votes, vote)
          .where(vote.user.id.eq(userId));
    }

    List<Post> posts = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(posts, pageable, posts.size());
  }
}
