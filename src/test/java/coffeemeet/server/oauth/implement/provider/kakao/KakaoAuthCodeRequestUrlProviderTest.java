package coffeemeet.server.oauth.implement.provider.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.oauth.config.kakao.KakaoProperties;
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
    // when
    OAuthProvider oAuthProvider = provider.oAuthProvider();

    // then
    assertThat(oAuthProvider).isEqualTo(OAuthProvider.KAKAO);
  }

  @DisplayName("카카오의 redirect url 을 제공할 수 있다.")
  @Test
  void provideTest() {
    // given
    String resultUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=testClientId&redirect_uri=https://testClientId/redirect";

    given(kakaoProperties.getClientId()).willReturn("testClientId");
    given(kakaoProperties.getRedirectUrl()).willReturn("https://testClientId/redirect");

    // when
    String expectedUrl = provider.provide();

    // then
    assertThat(resultUrl).isEqualTo(expectedUrl);
  }

}
