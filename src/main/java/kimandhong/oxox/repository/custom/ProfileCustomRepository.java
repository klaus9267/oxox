package kimandhong.oxox.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static kimandhong.oxox.domain.QProfile.profile;

@Repository
@RequiredArgsConstructor
public class ProfileCustomRepository {
  private final JPAQueryFactory jpaQueryFactory;

  public Long findMaxSequenceByNickname(final String nickname) {
    return jpaQueryFactory
        .select(profile.sequence.max().coalesce(0L))
        .from(profile)
        .where(profile.nickname.eq(nickname))
        .fetchOne();
  }
}
