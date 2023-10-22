package coffeemeet.server.auth.dto;

import coffeemeet.server.user.domain.Birth;
import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.OAuthProvider;

public record OAuthInfoResponse(
    String name,
    String profileImage,
    Birth birth,
    Email email,
    OAuthProvider oAuthProvider,
    String oAuthProviderId
) {

  public static OAuthInfoResponse of(
      String name,
      String profileImage,
      Birth birth,
      Email email,
      OAuthProvider oAuthProvider,
      String oAuthProviderId
  ) {
    return new OAuthInfoResponse(
        name,
        profileImage,
        birth,
        email,
        oAuthProvider,
        oAuthProviderId
    );
  }

}
