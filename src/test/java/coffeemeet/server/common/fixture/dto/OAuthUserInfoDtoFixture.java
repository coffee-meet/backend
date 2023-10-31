package coffeemeet.server.common.fixture.dto;

import static org.instancio.Select.field;

import coffeemeet.server.oauth.dto.OAuthUserInfoDto;
import org.instancio.Instancio;

public class OAuthUserInfoDtoFixture {

  public static OAuthUserInfoDto.Response response() {
    return Instancio.of(OAuthUserInfoDto.Response.class)
        .generate(field("birthYear"), gen -> gen.ints().range(1000, 9999).asString())
        .generate(field("birthDay"), gen -> gen.ints().range(1000, 9999).asString())
        .set(field("email"),"test123@gmail.com")
        .create();
  }

}
