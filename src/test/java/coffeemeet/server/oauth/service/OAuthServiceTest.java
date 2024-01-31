package coffeemeet.server.oauth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.oauth.implement.client.OAuthMemberClientRegistry;
import coffeemeet.server.oauth.implement.provider.AuthCodeRequestUrlProviderRegistry;
import coffeemeet.server.user.domain.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

  @InjectMocks
  private OAuthService oAuthService;

  @Mock
  private AuthCodeRequestUrlProviderRegistry authCodeRequestUrlProviderRegistry;

  @Mock
  private OAuthMemberClientRegistry oAuthMemberClientRegistry;

  @DisplayName("로그인 타입에 맞는 redirect url 을 생성할 수 있다.")
  @Test
  void getAuthCodeRequestUrlTest() {
    // given
    String expectedUrl = "https://example.com";

    given(authCodeRequestUrlProviderRegistry.provide(OAuthProvider.KAKAO)).willReturn(expectedUrl);

    // when
    String result = oAuthService.getAuthCodeRequestUrl(OAuthProvider.KAKAO);

    // then
    assertThat(result).isEqualTo(expectedUrl);
  }

}
