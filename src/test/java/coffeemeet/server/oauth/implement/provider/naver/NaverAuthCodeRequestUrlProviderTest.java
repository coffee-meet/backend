package coffeemeet.server.oauth.implement.provider.naver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.oauth.config.naver.NaverProperties;
import coffeemeet.server.user.domain.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NaverAuthCodeRequestUrlProviderTest {

  @Mock
  private NaverProperties naverProperties;

  @InjectMocks
  private NaverAuthCodeRequestUrlProvider provider;

  @DisplayName("네이버 프로바이더를 제공할 수 있다.")
  @Test
  void oAuthProviderTest() {
    // when
    OAuthProvider oAuthProvider = provider.oAuthProvider();

    // then
    assertThat(oAuthProvider).isEqualTo(OAuthProvider.NAVER);
  }

  @DisplayName("네이버의 redirect url 을 제공할 수 있다.")
  @Test
  void provideTest() {
    // given
    String resultUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=testClientId&redirect_uri=https://testClientId/redirect&state=https%253A%252F%252FtestClientId%252Fredirect";

    given(naverProperties.getClientId()).willReturn("testClientId");
    given(naverProperties.getRedirectUrl()).willReturn("https://testClientId/redirect");

    // when
    String expectedUrl = provider.provide();

    // then
    assertThat(resultUrl).isEqualTo(expectedUrl);
  }

}
