package coffeemeet.server.user.infrastructure;

public interface UserQueryDslRepository {

  boolean existsByNicknameAndNotUserId(String nickname, Long userId);

}
