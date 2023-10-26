package coffeemeet.server.oauth.dto;

import coffeemeet.server.user.domain.OAuthProvider;

public record OAuthInfoResponse(
    String name,
    String profileImage,
    String birthYear,
    String birthDay,
    String email,
    OAuthProvider oAuthProvider,
    String oAuthProviderId
) {

  public static OAuthInfoResponse of(
      String name,
      String profileImage,
      String birthYear,
      String birthDay,
      String email,
      OAuthProvider oAuthProvider,
      String oAuthProviderId
  ) {
    return new OAuthInfoResponse(
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
