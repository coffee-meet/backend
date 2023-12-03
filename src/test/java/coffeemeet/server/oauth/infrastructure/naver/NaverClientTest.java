package coffeemeet.server.oauth.infrastructure.naver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.fixture.dto.NaverMemberResponseFixture;
import coffeemeet.server.common.fixture.dto.NaverTokenFixture;
import coffeemeet.server.oauth.config.naver.NaverProperties;
import coffeemeet.server.oauth.infrastructure.naver.dto.NaverMemberDetail;
import coffeemeet.server.oauth.infrastructure.naver.dto.NaverTokens;
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
class NaverClientTest {


  @Mock
  private RestTemplate restTemplate;

  @Mock
  private NaverProperties naverProperties;

  @InjectMocks
  private NaverClient naverClient;

  @DisplayName("네이버로부터 인증 토큰을 받을 수 있다.")
  @Test
  void fetchTokenTest() {
    // given
    String authCode = "authCode";
    NaverTokens naverTokens = NaverTokenFixture.naverTokens();

    given(naverProperties.getClientId()).willReturn("testClientId");
    given(naverProperties.getRedirectUrl()).willReturn("testRedirectUrl");
    given(naverProperties.getClientSecret()).willReturn("testClientSecret");
    given(restTemplate.postForObject(
        anyString(),
        any(HttpEntity.class),
        eq(NaverTokens.class)))
        .willReturn(naverTokens);

    // when
    NaverTokens expectedTokens = naverClient.fetchToken(authCode);

    // then
    assertAll(
        () -> assertThat(expectedTokens).isNotNull(),
        () -> {
          assert expectedTokens != null;
          assertThat(expectedTokens.accessToken()).isEqualTo(naverTokens.accessToken());
        },
        () -> assertThat(Objects.requireNonNull(expectedTokens).refreshToken()).isEqualTo(
            naverTokens.refreshToken()),
        () -> assertThat(Objects.requireNonNull(expectedTokens).tokenType()).isEqualTo(
            naverTokens.tokenType()),
        () -> assertThat(Objects.requireNonNull(expectedTokens).expiresIn()).isEqualTo(
            naverTokens.expiresIn()),
        () -> assertThat(Objects.requireNonNull(expectedTokens).error()).isEqualTo(
            naverTokens.error())
    );
  }

  @DisplayName("네이버로부터 사용자 정보를 가져올 수 있다.")
  @Test
  void fetchMemberTest() {
    // given
    NaverMemberDetail response = NaverMemberResponseFixture.naverMemberResponse();
    ResponseEntity<NaverMemberDetail> mockResponse = ResponseEntity.ok(response);

    given(restTemplate.exchange(
        anyString(),
        any(HttpMethod.class),
        any(HttpEntity.class),
        eq(NaverMemberDetail.class)))
        .willReturn(mockResponse);

    // when
    NaverMemberDetail result = naverClient.fetchMember("accessToken");

    // then
    assertThat(result).isNotNull();
  }

}
