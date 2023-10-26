package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.oauth.dto.OAuthInfoDto;
import org.instancio.Instancio;

public class OAuthInfoResponseFixture {

  public static OAuthInfoDto.Response oAuthInfoResponse() {
    return Instancio.of(OAuthInfoDto.Response.class)
        .create();
  }

}
