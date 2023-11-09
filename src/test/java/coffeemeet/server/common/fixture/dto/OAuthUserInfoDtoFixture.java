package coffeemeet.server.common.fixture.dto;

import static org.instancio.Select.field;

import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import org.instancio.Instancio;

public class OAuthUserInfoDtoFixture {

  public static OAuthMemberDetail response() {
    return Instancio.of(OAuthMemberDetail.class)
        .set(field("email"), "test123@gmail.com")
        .create();
  }

}
