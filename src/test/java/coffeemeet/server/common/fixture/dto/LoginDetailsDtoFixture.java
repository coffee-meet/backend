package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.user.service.dto.LoginDetailsDto;
import org.instancio.Instancio;

public class LoginDetailsDtoFixture {

  public static LoginDetailsDto loginDetailsDto() {
    return Instancio.of(LoginDetailsDto.class)
        .create();
  }

}
