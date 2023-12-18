package coffeemeet.server.common.fixture;

import static org.instancio.Select.field;

import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberDetail;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoTokens;
import coffeemeet.server.oauth.infrastructure.naver.dto.NaverMemberDetail;
import coffeemeet.server.oauth.infrastructure.naver.dto.NaverTokens;
import org.instancio.Instancio;

public class OauthFixture {

  public static KakaoMemberDetail kakaoMemberResponse() {
    return Instancio.of(KakaoMemberDetail.class)
        .create();
  }

  public static KakaoTokens kakaoTokens() {
    return Instancio.of(KakaoTokens.class)
        .create();
  }

  public static NaverMemberDetail naverMemberResponse() {
    return Instancio.of(NaverMemberDetail.class)
        .create();
  }

  public static NaverTokens naverTokens() {
    return Instancio.of(NaverTokens.class)
        .create();
  }

  public static OAuthMemberDetail response() {
    return Instancio.of(OAuthMemberDetail.class)
        .set(field("email"), "test123@gmail.com")
        .create();
  }

}
