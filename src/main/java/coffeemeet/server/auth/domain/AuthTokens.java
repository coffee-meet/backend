package coffeemeet.server.auth.domain;

public record AuthTokens(
    String accessToken,
    String refreshToken
) {

  public static AuthTokens of(
      String accessToken,
      String refreshToken
  ) {
    return new AuthTokens(
        accessToken,
        refreshToken
    );
  }

}
