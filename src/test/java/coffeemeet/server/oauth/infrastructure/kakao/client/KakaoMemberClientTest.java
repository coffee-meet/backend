package coffeemeet.server.oauth.infrastructure.kakao.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import coffeemeet.server.common.fixture.dto.KakaoMemberResponseFixture;
import coffeemeet.server.common.fixture.dto.KakaoTokensFixture;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberResponse;
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
  KakaoApiClient kakaoApiClient;

  @DisplayName("카카오 프로바이더를 가져올 수 있다.")
  @Test
  void oAuthProvider() {
    // given, when, then
    assertThat(kakaoMemberClient.oAuthProvider()).isEqualTo(OAuthProvider.KAKAO);
  }

  @DisplayName("카카오로부터 사용자 정보를 가져올 수 있다.")
  @Test
  void fetch() {
    // given
    String authCode = "authCode";

    KakaoTokens kakaoTokens = KakaoTokensFixture.kakaoTokens();
    KakaoMemberResponse response = KakaoMemberResponseFixture.kakaoMemberResponse();

    // when
    when(kakaoApiClient.fetchToken(any())).thenReturn(kakaoTokens);
    when(kakaoApiClient.fetchMember(any())).thenReturn(response);

    // then
    assertThat(kakaoMemberClient.fetch(authCode)).isNotNull();
  }

}
