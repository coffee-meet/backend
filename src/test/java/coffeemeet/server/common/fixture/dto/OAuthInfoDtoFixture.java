package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.oauth.dto.OAuthInfoDto;
import org.instancio.Instancio;

public class OAuthInfoDtoFixture {

  public static OAuthInfoDto.Response response() {
    return Instancio.of(OAuthInfoDto.Response.class)
        .create();
  }

}
