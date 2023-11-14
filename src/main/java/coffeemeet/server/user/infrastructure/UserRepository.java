package coffeemeet.server.user.infrastructure;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.User;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsUserByOauthInfo(OAuthInfo oAuthInfo);

  Set<User> findByIdIn(Collection<Long> ids);

  Optional<User> findByOauthInfo(OAuthInfo oauthInfo);

  boolean existsUserByProfile_Nickname(String nickname);

  List<User> findAllByChattingRoom(ChattingRoom chattingRoom);

}
