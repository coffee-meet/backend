package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.oauth.dto.OAuthUserInfoDto;
import org.instancio.Instancio;

public class OAuthInfoResponseFixture {

  public static OAuthUserInfoDto.Response oAuthInfoResponse() {
    return Instancio.of(OAuthUserInfoDto.Response.class)
        .create();
  }

}
