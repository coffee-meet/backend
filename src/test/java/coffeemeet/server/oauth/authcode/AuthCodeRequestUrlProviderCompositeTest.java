package coffeemeet.server.oauth.authcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import coffeemeet.server.user.domain.OAuthProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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

  @Test
  void provideTest() {
    // given
    OAuthProvider oAuthProvider = OAuthProvider.KAKAO;
    String resultUrl = "https://kakao.com/login/oauth/authorize?client_id=testClientId&redirect_uri=https://testClientId/redirect";

    // when
    when(provider.oAuthProvider()).thenReturn(OAuthProvider.KAKAO);
    when(provider.provide()).thenReturn(resultUrl);

    // then
    providers = new HashSet<>(Collections.singletonList(provider));
    composite = new AuthCodeRequestUrlProviderComposite(providers);

    String expectedUrl = composite.provide(oAuthProvider);
    assertThat(resultUrl).isEqualTo(expectedUrl);
  }

}
