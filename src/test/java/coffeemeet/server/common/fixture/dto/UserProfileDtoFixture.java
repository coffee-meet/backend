package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.user.dto.UserProfileDto;
import org.instancio.Instancio;

public class UserProfileDtoFixture {

  public static UserProfileDto.Response userProfileDtoResponse() {
    return Instancio.of(UserProfileDto.Response.class)
        .create();
  }

}
