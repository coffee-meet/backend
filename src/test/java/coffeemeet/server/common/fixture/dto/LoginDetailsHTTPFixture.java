package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.user.presentation.dto.LoginDetailsHTTP;
import coffeemeet.server.user.presentation.dto.LoginDetailsHTTP.Response;
import coffeemeet.server.user.service.dto.LoginDetailsDto;

public class LoginDetailsHTTPFixture {

  public static LoginDetailsHTTP.Response loginDetailsHTTPResponse(
      LoginDetailsDto response) {
    return new Response(
        response.userId(),
        response.isRegistered(),
        response.accessToken(),
        response.refreshToken(),
        response.nickname(),
        response.profileImageUrl(),
        response.companyName(),
        response.department(),
        response.interests()
    );
  }

}
