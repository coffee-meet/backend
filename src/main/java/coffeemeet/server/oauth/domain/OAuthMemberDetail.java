package coffeemeet.server.oauth.domain;

import coffeemeet.server.user.domain.OAuthProvider;

public record OAuthMemberDetail(
    String name,
    String profileImage,
    String birthYear,
    String birthDay,
    String email,
    OAuthProvider oAuthProvider,
    String oAuthProviderId
) {

  public static OAuthMemberDetail of(
      String name,
      String profileImage,
      String birthYear,
      String birthDay,
      String email,
      OAuthProvider oAuthProvider,
      String oAuthProviderId
  ) {
    return new OAuthMemberDetail(
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
