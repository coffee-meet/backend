package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.auth.domain.LoginDetails;
import org.instancio.Instancio;

public class LoginDetailsFixture {

  public static LoginDetails loginDetails(){
    return Instancio.of(LoginDetails.class)
        .create();
  }

}
