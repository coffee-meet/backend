package coffeemeet.server.common.fixture;

import static org.instancio.Select.field;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.RefreshToken;
import org.instancio.Instancio;

public class AuthFixture {

  public static AuthTokens authTokens() {
    return Instancio.of(AuthTokens.class)
        .create();
  }

  public static AuthTokens authTokens(String refreshToken) {
    return Instancio.of(AuthTokens.class)
        .set(field(AuthTokens::refreshToken), refreshToken)
        .create();
  }

  public static RefreshToken refreshToken() {
    return Instancio.of(RefreshToken.class)
        .create();
  }

}
