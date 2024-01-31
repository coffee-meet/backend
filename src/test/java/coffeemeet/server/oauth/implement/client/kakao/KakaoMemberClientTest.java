package coffeemeet.server.oauth.implement.client.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.fixture.OauthFixture;
import coffeemeet.server.oauth.infrastructure.kakao.KakaoFetchClient;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberDetail;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoTokens;
import coffeemeet.server.user.domain.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KakaoMemberClientTest {

  @InjectMocks
  KakaoMemberClient kakaoMemberClient;

  @Mock
  KakaoFetchClient kakaoClient;

  @DisplayName("카카오 프로바이더를 가져올 수 있다.")
  @Test
  void oAuthProviderTest() {
    // when, then
    assertThat(kakaoMemberClient.oAuthProvider()).isEqualTo(OAuthProvider.KAKAO);
  }

  @DisplayName("카카오로부터 사용자 정보를 가져올 수 있다.")
  @Test
  void fetchTest() {
    // given
    String authCode = "authCode";
    KakaoTokens kakaoTokens = OauthFixture.kakaoTokens();
    KakaoMemberDetail response = OauthFixture.kakaoMemberResponse();

    given(kakaoClient.fetchToken(any())).willReturn(kakaoTokens);
    given(kakaoClient.fetchMember(any())).willReturn(response);

    // when, then
    assertThat(kakaoMemberClient.fetch(authCode)).isNotNull();
  }

}
