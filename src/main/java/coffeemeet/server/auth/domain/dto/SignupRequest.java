package coffeemeet.server.auth.domain.dto;

import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.domain.OAuthProvider;
import java.util.List;

public record SignupRequest(
    String nickname,
    List<Keyword> keywords,
    String authCode,
    OAuthProvider oAuthProvider
) {

  public static SignupRequest of(
      String nickname,
      List<Keyword> keywords,
      String authCode,
      OAuthProvider oAuthProvider
  ) {
    return new SignupRequest(
        nickname,
        keywords,
        authCode,
        oAuthProvider
    );
  }

}
