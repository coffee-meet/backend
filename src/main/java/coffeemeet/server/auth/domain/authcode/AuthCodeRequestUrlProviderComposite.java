package coffeemeet.server.auth.domain.authcode;

import coffeemeet.server.user.domain.OAuthProvider;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AuthCodeRequestUrlProviderComposite {

  private static final String INVALID_LOGIN_TYPE_MESSAGE = "로그인 타입(%s)에 일치하는 타입이 없습니다.";
  private final Map<OAuthProvider, AuthCodeRequestUrlProvider> mapping;

  public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers) {
    this.mapping = providers.stream().collect(
        Collectors.toUnmodifiableMap(
            AuthCodeRequestUrlProvider::oAuthProvider,
            Function.identity()
        )
    );
  }

  public String provide(OAuthProvider oAuthProvider) {
    return getProvider(oAuthProvider).provide();
  }

  private AuthCodeRequestUrlProvider getProvider(OAuthProvider oAuthProvider) {
    return Optional.ofNullable(mapping.get(oAuthProvider))
        .orElseThrow(() -> new IllegalArgumentException(
                String.format(INVALID_LOGIN_TYPE_MESSAGE, oAuthProvider)
            )
        );
  }

}
