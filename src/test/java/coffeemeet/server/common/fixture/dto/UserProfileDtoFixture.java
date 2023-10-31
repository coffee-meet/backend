package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.user.service.dto.UserProfileDto;
import org.instancio.Instancio;

public class UserProfileDtoFixture {

  public static UserProfileDto.Response userProfileDtoResponse() {
    return Instancio.of(UserProfileDto.Response.class)
        .create();
  }

}
