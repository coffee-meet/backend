package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoTokens;
import org.instancio.Instancio;

public class KakaoTokensFixture {

  public static KakaoTokens kakaoTokens() {
    return Instancio.of(KakaoTokens.class)
        .create();
  }

}
