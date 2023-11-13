package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.user.presentation.dto.UserProfileHTTP;
import coffeemeet.server.user.service.dto.UserProfileDto;

public class UserProfileHTTPFixture {

  public static UserProfileHTTP.Response userProfileHTTPResponse(UserProfileDto.Response response) {
    return new UserProfileHTTP.Response(
        response.nickname(),
        response.profileImageUrl(),
        response.department(),
        response.interests()
    );
  }

}
