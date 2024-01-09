package coffeemeet.server.user.infrastructure;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.User;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("""
      SELECT u
      FROM User u
      WHERE u.oauthInfo.oauthProvider = :oAuthProvider AND u.oauthInfo.oauthProviderId = :oauthProviderId
      """)
  Optional<User> findByOauthInfo(OAuthProvider oAuthProvider, String oauthProviderId);

  Set<User> findByIdIn(Collection<Long> ids);

  boolean existsUserByProfile_Nickname(String nickname);

  List<User> findAllByChattingRoom(ChattingRoom chattingRoom);

}
