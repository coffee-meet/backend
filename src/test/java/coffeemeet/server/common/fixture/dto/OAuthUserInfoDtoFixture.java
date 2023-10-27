package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.oauth.dto.OAuthUserInfoDto;
import org.instancio.Instancio;

public class OAuthUserInfoDtoFixture {

  public static OAuthUserInfoDto.Response response() {
    return Instancio.of(OAuthUserInfoDto.Response.class)
        .create();
  }

}
