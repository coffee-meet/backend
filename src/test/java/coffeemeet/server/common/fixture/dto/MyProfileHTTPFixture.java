package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.user.presentation.dto.MyProfileHTTP;
import coffeemeet.server.user.service.dto.MyProfileDto;

public class MyProfileHTTPFixture {

  public static MyProfileHTTP.Response myProfileHTTPResponse(MyProfileDto.Response response) {
    return new MyProfileHTTP.Response(
        response.nickname(),
        response.profileImageUrl(),
        response.companyName(),
        response.department(),
        response.interests()
    );
  }

}
