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

import java.time.LocalDateTime;
import java.util.List;

import static kimandhong.oxox.domain.QComment.comment;
import static kimandhong.oxox.domain.QPost.post;
import static kimandhong.oxox.domain.QReaction.reaction;
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
      query.leftJoin(post.votes, vote)
          .where(post.isDone.isFalse())
          .groupBy(post.id)
          .orderBy(vote.count().desc(), post.id.desc());
    } else if (PaginationSortType.HOT.equals(sortType)) {
      final LocalDateTime time = LocalDateTime.now().minusHours(1);
      query.leftJoin(post.votes, vote)
          .where(vote.createAt.after(time))
          .groupBy(post.id)
          .orderBy(vote.count().desc(), post.id.desc());
    } else if (PaginationSortType.BEST_REACTION.equals(sortType)) {
      query.leftJoin(post.comments, comment)
          .leftJoin(comment.reactions, reaction)
          .where(post.isDone.isFalse())
          .groupBy(post.id)
          .orderBy(reaction.count().desc(), post.id.desc());
    }

    List<Post> posts = query
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
      query.where(post.user.id.eq(userId), post.isDone.isFalse());
    } else if (PaginationSortType.JOIN.equals(sortType)) {
      query.leftJoin(post.votes, vote)
          .where(vote.user.id.eq(userId), post.isDone.isFalse());
    }

    List<Post> posts = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(posts, pageable, posts.size());
  }
}
