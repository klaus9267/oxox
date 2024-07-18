package kimandhong.oxox.repository.custom;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kimandhong.oxox.controller.param.SortType;
import kimandhong.oxox.domain.Post;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static kimandhong.oxox.domain.QComment.comment;
import static kimandhong.oxox.domain.QPost.post;
import static kimandhong.oxox.domain.QReaction.reaction;
import static kimandhong.oxox.domain.QVote.vote;

@Repository
@RequiredArgsConstructor
public class PostCustomRepository {
  private final JPAQueryFactory jpaQueryFactory;

  public List<Post> findAllSorted(SortType sortType) {
    if (SortType.JOIN.equals(sortType) || SortType.WRITER.equals(sortType)) {
      throw new BadRequestException(ErrorCode.WRONG_PARAMETER);
    }

    final Duration duration = SortType.HOT.equals(sortType) ? Duration.ofHours(1) : Duration.ofDays(1);
    final LocalDateTime time = LocalDateTime.now().minus(duration);

    final DateTimePath<LocalDateTime> datePath = post.createdAt;
    final JPAQuery<Post> query = jpaQueryFactory
        .selectFrom(post)
        .where(post.isDone.isFalse()
            .and(datePath.after(time)));

    switch (sortType) {
      case POPULARITY, HOT -> {
        query.leftJoin(post.votes, vote)
            .groupBy(post.id)
            .orderBy(vote.count().desc(), post.id.desc());
      }
      case BEST_REACTION -> {
        query.leftJoin(post.comments, comment)
            .leftJoin(comment.reactions, reaction)
            .groupBy(post.id)
            .orderBy(reaction.count().desc(), post.id.desc());
      }
      case CLOSE -> {
        query.leftJoin(post.votes, vote)
            .groupBy(post.id)
            .having(vote.isYes.when(true).then(1).otherwise(0).sum()
                .multiply(100.0).divide(vote.count())
                .subtract(50).abs().loe(5)
            ).orderBy(vote.count().desc(), post.id.desc());
      }
      default -> throw new BadRequestException(ErrorCode.BAD_REQUEST);
    }

    return query
        .distinct()
        .fetch();
  }

  public List<Post> findAllSortedWithUserId(SortType sortType, Long userId) {
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
