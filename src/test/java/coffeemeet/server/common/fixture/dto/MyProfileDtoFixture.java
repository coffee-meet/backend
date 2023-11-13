package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.user.service.dto.MyProfileDto;
import org.instancio.Instancio;

public class MyProfileDtoFixture {

  public static MyProfileDto.Response myProfileDtoResponse() {
    return Instancio.of(MyProfileDto.Response.class)
        .create();
  }

}
