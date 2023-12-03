package coffeemeet.server.oauth.infrastructure.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.fixture.dto.KakaoMemberResponseFixture;
import coffeemeet.server.common.fixture.dto.KakaoTokensFixture;
import coffeemeet.server.oauth.config.kakao.KakaoProperties;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberDetail;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoTokens;
import java.util.Objects;
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
class KakaoClientTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private KakaoProperties kakaoProperties;

  @InjectMocks
  private KakaoClient kakaoClient;

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
    KakaoTokens expectedTokens = kakaoClient.fetchToken(authCode);

    // then
    assertAll(
        () -> assertThat(expectedTokens).isNotNull(),
        () -> assertThat(Objects.requireNonNull(expectedTokens).accessToken()).isEqualTo(
            kakaoTokens.accessToken()),
        () -> assertThat(Objects.requireNonNull(expectedTokens).refreshToken()).isEqualTo(
            kakaoTokens.refreshToken()),
        () -> assertThat(Objects.requireNonNull(expectedTokens).tokenType()).isEqualTo(
            kakaoTokens.tokenType()),
        () -> assertThat(Objects.requireNonNull(expectedTokens).expiresIn()).isEqualTo(
            kakaoTokens.expiresIn()),
        () -> assertThat(Objects.requireNonNull(expectedTokens).refreshTokenExpiresIn()).isEqualTo(
            kakaoTokens.refreshTokenExpiresIn())
    );
  }

  @DisplayName("카카오로부터 사용자 정보를 가져올 수 있다.")
  @Test
  void fetchMemberTest() {
    // given
    KakaoMemberDetail response = KakaoMemberResponseFixture.kakaoMemberResponse();
    ResponseEntity<KakaoMemberDetail> mockResponse = ResponseEntity.ok(response);

    given(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        any(HttpEntity.class),
        eq(KakaoMemberDetail.class)))
        .willReturn(mockResponse);

    // when
    KakaoMemberDetail result = kakaoClient.fetchMember("accessToken");

    // then
    assertThat(result).isNotNull();
  }

}
