package coffeemeet.server.oauth.dto;

import coffeemeet.server.user.domain.OAuthProvider;

public sealed interface OAuthUserInfoDto permits OAuthUserInfoDto.Response {

  record Response(
      String name,
      String profileImage,
      String birthYear,
      String birthDay,
      String email,
      OAuthProvider oAuthProvider,
      String oAuthProviderId
  ) implements OAuthUserInfoDto {

    public static OAuthUserInfoDto.Response of(
        String name,
        String profileImage,
        String birthYear,
        String birthDay,
        String email,
        OAuthProvider oAuthProvider,
        String oAuthProviderId
    ) {
      return new OAuthUserInfoDto.Response(
          name,
          profileImage,
          birthYear,
          birthDay,
          email,
          oAuthProvider,
          oAuthProviderId
      );
    }
  }

}
