package coffeemeet.server.user.infrastructure;

import static coffeemeet.server.user.domain.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryDslRepositoryImpl implements UserQueryDslRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public boolean existsByNicknameAndNotUserId(String nickname, Long userId) {
    return jpaQueryFactory
        .selectOne()
        .from(user)
        .where(user.profile.nickname.eq(nickname).and(user.id.ne(userId)))
        .fetchFirst() != null;
  }

}
