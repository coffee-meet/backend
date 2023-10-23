package coffeemeet.server.user.repository;

import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsUserByOauthInfo_oauthProviderAndOauthInfo_oauthProviderId(
      OAuthProvider oauthProvider,
      String oauthProviderId);

  Optional<User> getUserByOauthInfoOauthProviderAndOauthInfoOauthProviderId(
      OAuthProvider oAuthProvider,
      String authCode);
}
