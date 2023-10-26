package coffeemeet.server.oauth.infrastructure.kakao.authcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import coffeemeet.server.oauth.infrastructure.kakao.config.KakaoProperties;
import coffeemeet.server.user.domain.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KakaoAuthCodeRequestUrlProviderTest {

  @Mock
  private KakaoProperties kakaoProperties;

  @InjectMocks
  private KakaoAuthCodeRequestUrlProvider provider;

  @DisplayName("카카오 프로바이더를 제공할 수 있다.")
  @Test
  void oAuthProviderTest() {
    // given
    OAuthProvider oAuthProvider = provider.oAuthProvider();

    // when, then
    assertThat(oAuthProvider).isEqualTo(OAuthProvider.KAKAO);
  }

  @DisplayName("카카오의 redirect url 을 제공할 수 있다.")
  @Test
  void provideTest() {
    // given
    String resultUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=testClientId&redirect_uri=https://testClientId/redirect";

    // when
    when(kakaoProperties.getClientId()).thenReturn("testClientId");
    when(kakaoProperties.getRedirectUrl()).thenReturn("https://testClientId/redirect");

    // then
    String expectedUrl = provider.provide();
    assertThat(resultUrl).isEqualTo(expectedUrl);
  }

}
