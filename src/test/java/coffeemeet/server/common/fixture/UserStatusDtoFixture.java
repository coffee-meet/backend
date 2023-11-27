package coffeemeet.server.common.fixture;

import coffeemeet.server.user.service.dto.UserStatusDto;
import org.instancio.Instancio;

public class UserStatusDtoFixture {

  public static UserStatusDto userStatusDto() {
    return Instancio.of(UserStatusDto.class)
        .create();
  }

}
