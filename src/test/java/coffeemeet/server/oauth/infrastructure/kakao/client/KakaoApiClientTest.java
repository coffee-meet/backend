package coffeemeet.server.oauth.infrastructure.kakao.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.fixture.dto.KakaoMemberResponseFixture;
import coffeemeet.server.common.fixture.dto.KakaoTokensFixture;
import coffeemeet.server.oauth.infrastructure.kakao.config.KakaoProperties;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberResponse;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoTokens;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class KakaoApiClientTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private KakaoProperties kakaoProperties;

  @InjectMocks
  private KakaoApiClient kakaoApiClient;

  @DisplayName("카카오로부터 인증 토큰을 받을 수 있다.")
  @Test
  void fetchTokenTest() {
    // given
    String authCode = "authCode";
    KakaoTokens kakaoTokens = KakaoTokensFixture.kakaoTokens();

    given(kakaoProperties.getClientId()).willReturn("testClientId");
    given(kakaoProperties.getRedirectUrl()).willReturn("testRedirectUrl");
    given(kakaoProperties.getClientSecret()).willReturn("testClientSecret");
    given(restTemplate.postForObject(
        anyString(),
        any(HttpEntity.class),
        eq(KakaoTokens.class)))
        .willReturn(kakaoTokens);

    // when
    KakaoTokens expectedTokens = kakaoApiClient.fetchToken(authCode);

    // then
    assertAll(
        () -> assertThat(expectedTokens).isNotNull(),
        () -> assertThat(expectedTokens.accessToken()).isEqualTo(kakaoTokens.accessToken()),
        () -> assertThat(expectedTokens.refreshToken()).isEqualTo(kakaoTokens.refreshToken()),
        () -> assertThat(expectedTokens.tokenType()).isEqualTo(kakaoTokens.tokenType()),
        () -> assertThat(expectedTokens.expiresIn()).isEqualTo(kakaoTokens.expiresIn()),
        () -> assertThat(expectedTokens.refreshTokenExpiresIn()).isEqualTo(
            kakaoTokens.refreshTokenExpiresIn())
    );
  }

  @DisplayName("카카오로부터 사용자 정보를 가져올 수 있다.")
  @Test
  void fetchMemberTest() {
    // given
    KakaoMemberResponse response = KakaoMemberResponseFixture.kakaoMemberResponse();
    ResponseEntity<KakaoMemberResponse> mockResponse = ResponseEntity.ok(response);

    given(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        any(HttpEntity.class),
        eq(KakaoMemberResponse.class)))
        .willReturn(mockResponse);

    // when
    KakaoMemberResponse result = kakaoApiClient.fetchMember("accessToken");

    // then
    assertThat(result).isNotNull();
  }

}
