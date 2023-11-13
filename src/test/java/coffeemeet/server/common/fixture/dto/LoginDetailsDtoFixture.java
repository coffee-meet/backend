package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.user.service.dto.LoginDetailsDto;
import org.instancio.Instancio;

public class LoginDetailsDtoFixture {

  public static LoginDetailsDto.Response loginDetailsDto(){
    return Instancio.of(LoginDetailsDto.Response.class)
        .create();
  }

}
