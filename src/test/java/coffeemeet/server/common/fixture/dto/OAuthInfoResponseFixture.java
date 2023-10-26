package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.oauth.dto.OAuthInfoResponse;
import org.instancio.Instancio;

public class OAuthInfoResponseFixture {

  public static OAuthInfoResponse oAuthInfoResponse() {
    return Instancio.of(OAuthInfoResponse.class)
        .create();
  }

}
