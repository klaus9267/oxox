package kimandhong.oxox.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kimandhong.oxox.controller.param.PostCondition;
import kimandhong.oxox.dto.post.PostDto;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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

  public Page<PostDto> findAll(final PostCondition postCondition, final Pageable pageable) {
    final Duration duration = PostCondition.HOT.equals(postCondition) ? Duration.ofHours(1) : Duration.ofDays(1);
    final LocalDateTime time = LocalDateTime.now().minus(duration);

    JPAQuery<PostDto> query = this.createBaseQuery(pageable);
    switch (postCondition == null ? PostCondition.DEFAULT : postCondition) {
      case POPULARITY, HOT -> {
        query.leftJoin(post.votes, vote)
            .where(post.isDone.isFalse())
            .groupBy(post.id)
            .orderBy(vote.count().desc(), post.id.desc());
      }
      case BEST_REACTION -> {
        query.leftJoin(post.comments, comment)
            .leftJoin(comment.reactions, reaction)
            .where(post.isDone.isFalse())
            .groupBy(post.id)
            .orderBy(reaction.count().desc(), post.id.desc());
      }
      case CLOSE -> {
        query.leftJoin(post.votes, vote)
            .where(post.isDone.isFalse())
            .groupBy(post.id)
            .having(vote.isYes.when(true).then(1).otherwise(0).sum()
                .multiply(100.0).divide(vote.count())
                .subtract(50).abs().loe(5)
            ).orderBy(vote.count().desc(), post.id.desc());
      }
      case DEFAULT -> query.orderBy(post.id.desc());
      default -> throw new BadRequestException(ErrorCode.BAD_REQUEST);
    }

    final List<PostDto> contents = query
        .where(post.createdAt.after(time))
        .fetch();

    final JPAQuery<Long> count = this.createCountQuery().where(post.createdAt.after(time));

    return PageableExecutionUtils.getPage(contents, pageable, count::fetchOne);
  }

  public Page<PostDto> findAllWithUserId(final PostCondition postCondition, final Pageable pageable, Long userId) {
    JPAQuery<PostDto> query = this.createBaseQuery(pageable);
    final BooleanBuilder builder = new BooleanBuilder();

    switch (postCondition) {
      case WRITER -> {
        builder.and(post.user.id.eq(userId));
      }
      case JOIN -> {
        builder.and(post.votes.any().user.id.eq(userId));
      }
      default -> throw new BadRequestException(ErrorCode.BAD_REQUEST);
    }

    final JPAQuery<Long> count = this.createCountQuery().where(builder);
    final List<PostDto> postDtos = query.where(builder).fetch();

    return PageableExecutionUtils.getPage(postDtos, pageable, count::fetchOne);
  }

  private JPAQuery<PostDto> createBaseQuery(final Pageable pageable) {
    return jpaQueryFactory.select(
            Projections.constructor(PostDto.class,
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
            )
        ).from(post)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());
  }

  private JPAQuery<Long> createCountQuery() {
    return jpaQueryFactory.select(post.count())
        .from(post);
  }
}
