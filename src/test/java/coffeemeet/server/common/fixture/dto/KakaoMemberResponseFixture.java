package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberResponse;
import org.instancio.Instancio;

public class KakaoMemberResponseFixture {

  public static KakaoMemberResponse kakaoMemberResponse() {
    return Instancio.of(KakaoMemberResponse.class)
        .create();
  }

}
