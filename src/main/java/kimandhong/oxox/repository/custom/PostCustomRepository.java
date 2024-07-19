package kimandhong.oxox.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kimandhong.oxox.controller.param.SortType;
import kimandhong.oxox.dto.post.PostDto;
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
import static kimandhong.oxox.domain.QUser.user;
import static kimandhong.oxox.domain.QVote.vote;

@Repository
@RequiredArgsConstructor
public class PostCustomRepository {
  private final JPAQueryFactory jpaQueryFactory;

  public List<PostDto> findAllSorted(SortType sortType) {
    if (SortType.JOIN.equals(sortType) || SortType.WRITER.equals(sortType)) {
      throw new BadRequestException(ErrorCode.WRONG_PARAMETER);
    }

    final Duration duration = SortType.HOT.equals(sortType) ? Duration.ofHours(1) : Duration.ofDays(1);
    final LocalDateTime time = LocalDateTime.now().minus(duration);

    final DateTimePath<LocalDateTime> datePath = post.createdAt;
    final JPAQuery<PostDto> query = this.createGetPostDtosQuery()
        .where(post.isDone.isFalse(), datePath.after(time));

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

    return query.fetch();
  }

  public List<PostDto> findAllSortedWithUserId(SortType sortType, Long userId) {
    final JPAQuery<PostDto> query = this.createGetPostDtosQuery();

    switch (sortType) {
      case WRITER -> {
        query.leftJoin(post.user, user).fetchJoin()
            .where(user.id.eq(userId))
            .distinct();
      }
      case JOIN -> {
        query.leftJoin(post.votes, vote)
            .where(vote.user.id.eq(userId));
      }
      default -> throw new BadRequestException(ErrorCode.BAD_REQUEST);
    }

    return query.fetch();
  }

  private JPAQuery<PostDto> createGetPostDtosQuery() {
    return jpaQueryFactory
        .select(Projections.constructor(PostDto.class,
            post.id,
            post.title,
            post.thumbnail,
            post.createdAt,
            post.isDone,
            post.comments.size(),
            JPAExpressions.select(vote.count())
                .from(vote)
                .where(vote.post.eq(post).and(vote.isYes.isTrue())),
            JPAExpressions.select(vote.count())
                .from(vote)
                .where(vote.post.eq(post).and(vote.isYes.isFalse()))
        ))
        .from(post);
  }
}
