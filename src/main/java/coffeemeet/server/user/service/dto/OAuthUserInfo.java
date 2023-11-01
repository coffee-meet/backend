package coffeemeet.server.user.service.dto;

import coffeemeet.server.oauth.dto.OAuthUserInfoDto;
import coffeemeet.server.user.domain.OAuthProvider;

public record OAuthUserInfo(
    String name,
    String profileImage,
    String birthYear,
    String birthDay,
    String email,
    OAuthProvider oAuthProvider,
    String oAuthProviderId
) {

  public static OAuthUserInfo from(OAuthUserInfoDto.Response response) {
    return new OAuthUserInfo(
        response.name(),
        response.profileImage(),
        response.birthYear(),
        response.birthDay(),
        response.email(),
        response.oAuthProvider(),
        response.oAuthProviderId()
    );
  }

}
