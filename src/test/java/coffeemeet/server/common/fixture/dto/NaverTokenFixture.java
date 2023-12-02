package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.oauth.infrastructure.naver.dto.NaverTokens;
import org.instancio.Instancio;

public class NaverTokenFixture {

  public static NaverTokens naverTokens() {
    return Instancio.of(NaverTokens.class)
        .create();
  }

}
