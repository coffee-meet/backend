package coffeemeet.server.oauth.dto;

import coffeemeet.server.user.domain.OAuthProvider;

public sealed interface OAuthInfoDto permits OAuthInfoDto.Response {

  record Response(
      String name,
      String profileImage,
      String birthYear,
      String birthDay,
      String email,
      OAuthProvider oAuthProvider,
      String oAuthProviderId
  ) implements OAuthInfoDto {

    public static OAuthInfoDto.Response of(
        String name,
        String profileImage,
        String birthYear,
        String birthDay,
        String email,
        OAuthProvider oAuthProvider,
        String oAuthProviderId
    ) {
      return new OAuthInfoDto.Response(
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
