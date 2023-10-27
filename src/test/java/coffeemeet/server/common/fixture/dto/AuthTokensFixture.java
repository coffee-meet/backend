package coffeemeet.server.common.fixture.dto;

import static org.instancio.Select.field;

import coffeemeet.server.auth.domain.AuthTokens;
import org.instancio.Instancio;

public class AuthTokensFixture {

  public static AuthTokens authTokens() {
    return Instancio.of(AuthTokens.class)
        .create();
  }

  public static AuthTokens authTokens(String refreshToken) {
    return Instancio.of(AuthTokens.class)
        .set(field(AuthTokens::refreshToken), refreshToken)
        .create();
  }

}
