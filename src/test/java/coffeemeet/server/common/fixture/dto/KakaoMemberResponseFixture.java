package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberDetail;
import org.instancio.Instancio;

public class KakaoMemberResponseFixture {

  public static KakaoMemberDetail kakaoMemberResponse() {
    return Instancio.of(KakaoMemberDetail.class)
        .create();
  }

}
