package coffeemeet.server.oauth.implement.client;

import static coffeemeet.server.auth.exception.AuthErrorCode.INVALID_LOGIN_TYPE;

import coffeemeet.server.common.execption.InvalidAuthException;
import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.user.domain.OAuthProvider;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OAuthMemberClientRegistry {

  private static final String INVALID_LOGIN_TYPE_MESSAGE = "로그인 타입(%s)에 일치하는 타입이 없습니다.";

  private final Map<OAuthProvider, OAuthMemberClient> mapping;

  public OAuthMemberClientRegistry(Set<OAuthMemberClient> clients) {
    this.mapping = clients.stream().collect(
        Collectors.toUnmodifiableMap(OAuthMemberClient::oAuthProvider, Function.identity())
    );
  }

  public OAuthMemberDetail fetch(OAuthProvider oAuthProvider, String authCode) {
    return getClient(oAuthProvider).fetch(authCode);
  }

  private OAuthMemberClient getClient(OAuthProvider oAuthProvider) {
    return Optional.ofNullable(mapping.get(oAuthProvider))
        .orElseThrow(() -> new InvalidAuthException(
            INVALID_LOGIN_TYPE,
            String.format(INVALID_LOGIN_TYPE_MESSAGE, oAuthProvider))
        );
  }

}
