package coffeemeet.server.oauth.authcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.user.domain.OAuthProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthCodeRequestUrlProviderCompositeTest {

  @Mock
  private AuthCodeRequestUrlProvider provider;

  @Mock
  private Set<AuthCodeRequestUrlProvider> providers;

  @InjectMocks
  private AuthCodeRequestUrlProviderComposite composite;

  @DisplayName("sns 의 redirect url 을 제공할 수 있다.")
  @Test
  void provideTest() {
    // given
    OAuthProvider oAuthProvider = OAuthProvider.KAKAO;
    String resultUrl = "https://kakao.com/login/oauth/authorize?client_id=testClientId&redirect_uri=https://testClientId/redirect";

    given(provider.oAuthProvider()).willReturn(OAuthProvider.KAKAO);
    given(provider.provide()).willReturn(resultUrl);

    providers = new HashSet<>(Collections.singletonList(provider));
    composite = new AuthCodeRequestUrlProviderComposite(providers);

    // when
    String expectedUrl = composite.provide(oAuthProvider);

    // then
    assertThat(resultUrl).isEqualTo(expectedUrl);
  }

}
