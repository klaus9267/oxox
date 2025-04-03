package kimandhong.oxox.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kimandhong.oxox.application.handler.error.CustomException;
import kimandhong.oxox.application.handler.error.ErrorCode;
import kimandhong.oxox.domain.post.dto.PostDto;
import kimandhong.oxox.domain.post.params.PostCondition;
import kimandhong.oxox.domain.vote.QVote;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static kimandhong.oxox.domain.comment.domain.QComment.comment;
import static kimandhong.oxox.domain.post.domain.QPost.post;
import static kimandhong.oxox.domain.reaction.domain.QReaction.reaction;
import static kimandhong.oxox.domain.vote.QVote.vote;

@Repository
@RequiredArgsConstructor
public class PostCustomRepository {
  private final JPAQueryFactory jpaQueryFactory;

  public Page<PostDto> findAll(final PostCondition postCondition, final Pageable pageable) {
    final Duration duration = PostCondition.HOT.equals(postCondition) ? Duration.ofHours(1) : Duration.ofDays(1);
    final LocalDateTime time = LocalDateTime.now().minus(duration);

    JPAQuery<PostDto> query = this.createBaseQuery(pageable);
    switch (postCondition == null ? PostCondition.DEFAULT : postCondition) {
      case POPULARITY, HOT -> query.leftJoin(post.oneToMany.votes, vote)
          .where(post.isDone.isFalse())
          .groupBy(post.id)
          .orderBy(vote.count().desc(), post.id.desc());
      case BEST_REACTION -> query.leftJoin(post.oneToMany.comments, comment)
          .leftJoin(comment.oneToMany.reactions, reaction)
          .where(post.isDone.isFalse())
          .groupBy(post.id)
          .orderBy(reaction.count().desc(), post.id.desc());
      case CLOSE -> query.leftJoin(post.oneToMany.votes, vote)
          .where(post.isDone.isFalse())
          .groupBy(post.id)
          .having(vote.isYes.when(true).then(1).otherwise(0).sum()
              .multiply(100.0).divide(vote.count())
              .subtract(50).abs().loe(5)
          ).orderBy(vote.count().desc(), post.id.desc());
      case DEFAULT -> query.orderBy(post.id.desc());
      default -> throw new CustomException(ErrorCode.BAD_REQUEST);
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
      case WRITER -> builder.and(post.user.id.eq(userId));
      case JOIN -> builder.and(post.oneToMany.votes.any().user.id.eq(userId));
      default -> throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    final JPAQuery<Long> count = this.createCountQuery().where(builder);
    final List<PostDto> postDtos = query.where(builder)
        .orderBy(post.id.desc())
        .fetch();

    return PageableExecutionUtils.getPage(postDtos, pageable, count::fetchOne);
  }

  private JPAQuery<PostDto> createBaseQuery(final Pageable pageable) {
    QVote voteYes = new QVote("voteYes");
    QVote voteNo = new QVote("voteNo");

    return jpaQueryFactory.select(
            Projections.constructor(PostDto.class,
                post.id,
                post.title,
                post.thumbnail,
                post.createdAt,
                post.isDone,
                post.oneToMany.comments.size(),
                voteYes.count(),
                voteNo.count()
            )
        ).from(post)
        .leftJoin(voteYes).on(voteYes.post.eq(post).and(voteYes.isYes.isTrue()))
        .leftJoin(voteNo).on(voteNo.post.eq(post).and(voteNo.isYes.isFalse()))
        .groupBy(post.id)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());
  }

  private JPAQuery<Long> createCountQuery() {
    return jpaQueryFactory.select(post.count())
        .from(post);
  }
}
