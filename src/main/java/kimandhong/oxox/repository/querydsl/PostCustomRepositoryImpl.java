package kimandhong.oxox.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kimandhong.oxox.controller.param.SortType;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.BadRequestException;
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
  public List<Post> findAllWithSort(SortType sortType) {
    if (SortType.JOIN.equals(sortType) || SortType.WRITER.equals(sortType)) {
      throw new BadRequestException(ErrorCode.WRONG_PARAMETER);
    }

    final LocalDateTime time = LocalDateTime.now().minusDays(1);
    final DateTimePath<LocalDateTime> datePath = post.createdAt;
    final JPAQuery<Post> query = jpaQueryFactory.selectFrom(post);
    final BooleanBuilder builder = new BooleanBuilder()
        .and(datePath.after(time))
        .and(post.isDone.isFalse());


    if (SortType.POPULARITY.equals(sortType)) {
      query.leftJoin(post.votes, vote)
          .groupBy(post.id)
          .orderBy(vote.count().desc(), post.id.desc());
    } else if (SortType.HOT.equals(sortType)) {
      query.leftJoin(post.votes, vote)
          .groupBy(post.id)
          .orderBy(vote.count().desc(), post.id.desc());
    } else if (SortType.BEST_REACTION.equals(sortType)) {
      query.leftJoin(post.comments, comment)
          .leftJoin(comment.reactions, reaction)
          .orderBy(reaction.count().desc(), post.id.desc());
    }

    return query
        .where(builder)
        .groupBy(post.id)
        .fetch();
  }

  @Override
  public List<Post> findAllWithSorAndUserId(SortType sortType, Long userId) {
    final LocalDateTime time = LocalDateTime.now().minusDays(1);
    final JPAQuery<Post> query = jpaQueryFactory.selectFrom(post)
        .where(post.createdAt.goe(time));

    if (SortType.WRITER.equals(sortType)) {
      query.where(post.user.id.eq(userId), post.isDone.isFalse());
    } else if (SortType.JOIN.equals(sortType)) {
      query.leftJoin(post.votes, vote)
          .where(vote.user.id.eq(userId), post.isDone.isFalse());
    }

    return query.fetch();

  }
}
