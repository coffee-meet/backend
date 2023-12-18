package coffeemeet.server.oauth.implement.client.naver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.fixture.dto.NaverMemberResponseFixture;
import coffeemeet.server.common.fixture.dto.NaverTokenFixture;
import coffeemeet.server.oauth.infrastructure.naver.NaverClient;
import coffeemeet.server.oauth.infrastructure.naver.dto.NaverMemberDetail;
import coffeemeet.server.oauth.infrastructure.naver.dto.NaverTokens;
import coffeemeet.server.user.domain.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NaverMemberClientTest {

  @InjectMocks
  private NaverMemberClient naverMemberClient;

  @Mock
  private NaverClient naverClient;

  @DisplayName("네이버 프로바이더를 가져올 수 있다.")
  @Test
  void oAuthProviderTest() {
    // when, then
    assertThat(naverMemberClient.oAuthProvider()).isEqualTo(OAuthProvider.NAVER);
  }

  @DisplayName("네이버로부터 사용자 정보를 가져올 수 있다.")
  @Test
  void fetchTest() {
    // given
    String authCode = "authCode";
    NaverTokens naverTokens = NaverTokenFixture.naverTokens();
    NaverMemberDetail response = NaverMemberResponseFixture.naverMemberResponse();

    given(naverClient.fetchToken(any())).willReturn(naverTokens);
    given(naverClient.fetchMember(any())).willReturn(response);

    // when, then
    assertThat(naverMemberClient.fetch(authCode)).isNotNull();
  }

}
