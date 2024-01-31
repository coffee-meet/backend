package coffeemeet.server.oauth.implement.client;

import static coffeemeet.server.auth.exception.AuthErrorCode.INVALID_LOGIN_TYPE;

import coffeemeet.server.common.execption.InvalidAuthException;
import coffeemeet.server.oauth.infrastructure.OAuthUnlinkDetail;
import coffeemeet.server.user.domain.OAuthProvider;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OAuthMemberUnlinkRegistry {

  private static final String INVALID_LOGIN_TYPE_MESSAGE = "로그인 타입(%s)에 일치하는 타입이 없습니다.";

  private final Map<OAuthProvider, OAuthUnlinkClient> mapping;

  public OAuthMemberUnlinkRegistry(Set<OAuthUnlinkClient> details) {
    this.mapping = details.stream().collect(
        Collectors.toUnmodifiableMap(OAuthUnlinkClient::oAuthProvider, Function.identity())
    );
  }

  public OAuthUnlinkDetail unlink(OAuthProvider oAuthProvider, String accessToken) {
    return getClient(oAuthProvider).unlink(accessToken);
  }

  private OAuthUnlinkClient getClient(OAuthProvider oAuthProvider) {
    return Optional.ofNullable(mapping.get(oAuthProvider))
        .orElseThrow(() -> new InvalidAuthException(
            INVALID_LOGIN_TYPE,
            String.format(INVALID_LOGIN_TYPE_MESSAGE, oAuthProvider))
        );
  }

}
