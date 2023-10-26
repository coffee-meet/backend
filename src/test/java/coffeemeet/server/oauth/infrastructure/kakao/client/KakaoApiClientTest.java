package coffeemeet.server.oauth.infrastructure.kakao.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import coffeemeet.server.common.fixture.dto.KakaoMemberResponseFixture;
import coffeemeet.server.common.fixture.dto.KakaoTokensFixture;
import coffeemeet.server.oauth.infrastructure.kakao.config.KakaoProperties;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberResponse;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoTokens;
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

  @Test
  void fetchTokenTest() {
    // given
    String authCode = "authCode";
    KakaoTokens kakaoTokens = KakaoTokensFixture.kakaoTokens();

    // when
    when(kakaoProperties.getClientId()).thenReturn("testClientId");
    when(kakaoProperties.getRedirectUrl()).thenReturn("testRedirectUrl");
    when(kakaoProperties.getClientSecret()).thenReturn("testClientSecret");
    when(restTemplate.postForObject(
        anyString(),
        any(HttpEntity.class),
        eq(KakaoTokens.class)))
        .thenReturn(kakaoTokens);

    // then
    KakaoTokens expectedTokens = kakaoApiClient.fetchToken(authCode);
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

  @Test
  void fetchMemberTest() {
    // given
    KakaoMemberResponse response = KakaoMemberResponseFixture.kakaoMemberResponse();
    ResponseEntity<KakaoMemberResponse> mockResponse = ResponseEntity.ok(response);

    // when
    when(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        any(HttpEntity.class),
        eq(KakaoMemberResponse.class)))
        .thenReturn(mockResponse);

    // then
    KakaoMemberResponse result = kakaoApiClient.fetchMember("accessToken");
    assertThat(result).isNotNull();
  }

}
