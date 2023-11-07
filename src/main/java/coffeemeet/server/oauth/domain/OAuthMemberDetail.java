package coffeemeet.server.oauth.domain;

import coffeemeet.server.user.domain.OAuthProvider;

public record OAuthMemberDetail(
    String profileImage,
    String email,
    OAuthProvider oAuthProvider,
    String oAuthProviderId
) {

  public static OAuthMemberDetail of(
      String profileImage,
      String email,
      OAuthProvider oAuthProvider,
      String oAuthProviderId
  ) {
    return new OAuthMemberDetail(
        profileImage,
        email,
        oAuthProvider,
        oAuthProviderId
    );
  }

}
