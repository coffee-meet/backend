package coffeemeet.server.user.domain;

import static java.util.Locale.ENGLISH;

public enum OAuthProvider {
  KAKAO,
  NAVER,
  ;

  public static OAuthProvider from(String type) {
    return OAuthProvider.valueOf(type.toUpperCase(ENGLISH));
  }

}
