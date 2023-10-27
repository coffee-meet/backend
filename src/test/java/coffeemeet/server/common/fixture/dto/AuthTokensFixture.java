package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.auth.domain.AuthTokens;
import org.instancio.Instancio;

public class AuthTokensFixture {

  public static AuthTokens authTokens() {
    return Instancio.of(AuthTokens.class)
        .create();
  }

}
