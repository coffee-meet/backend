package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.oauth.infrastructure.naver.dto.NaverMemberDetail;
import org.instancio.Instancio;

public class NaverMemberResponseFixture {

  public static NaverMemberDetail naverMemberResponse() {
    return Instancio.of(NaverMemberDetail.class)
        .create();
  }
}
