package coffeemeet.server.oauth.client;

import coffeemeet.server.oauth.dto.OAuthInfoDto;
import coffeemeet.server.user.domain.OAuthProvider;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OAuthMemberClientComposite {

  private static final String INVALID_LOGIN_TYPE_MESSAGE = "로그인 타입(%s)에 일치하는 타입이 없습니다.";
  private final Map<OAuthProvider, OAuthMemberClient> mapping;

  public OAuthMemberClientComposite(Set<OAuthMemberClient> clients) {
    this.mapping = clients.stream().collect(
        Collectors.toUnmodifiableMap(OAuthMemberClient::oAuthProvider, Function.identity())
    );
  }

  public OAuthInfoDto.Response fetch(OAuthProvider oAuthProvider, String authCode) {
    return getClient(oAuthProvider).fetch(authCode);
  }

  private OAuthMemberClient getClient(OAuthProvider oAuthProvider) {
    return Optional.ofNullable(mapping.get(oAuthProvider))
        .orElseThrow(() -> new IllegalArgumentException(
            String.format(INVALID_LOGIN_TYPE_MESSAGE, oAuthProvider))
        );
  }

}
