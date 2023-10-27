package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.auth.domain.RefreshToken;
import org.instancio.Instancio;

public class RefreshTokenFixture {

  public static RefreshToken refreshToken() {
    return Instancio.of(RefreshToken.class)
        .create();
  }

}
