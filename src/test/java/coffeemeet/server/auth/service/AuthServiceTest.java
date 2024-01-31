package coffeemeet.server.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.auth.implement.RefreshTokenCommand;
import coffeemeet.server.common.execption.InvalidAuthException;
import coffeemeet.server.common.fixture.AuthFixture;
import coffeemeet.server.oauth.service.OAuthService;
import coffeemeet.server.user.service.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  private static final String REFRESH_TOKEN = "Bearer aaaaaaaa.bbbbbbb.ccccccc";

  @InjectMocks
  private AuthService authService;

  @InjectMocks
  private OAuthService oAuthService;

  @Mock
  private UserService userService;

  @Mock
  private AuthTokensGenerator authTokensGenerator;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @Mock
  private RefreshTokenCommand refreshTokenCommand;

  @DisplayName("access token 을 갱신할 수 있다.")
  @Test
  void renewTest() {
    // given
    AuthTokens authTokens = AuthFixture.authTokens();
    AuthTokens newAuthTokens = AuthFixture.authTokens(REFRESH_TOKEN);

    given(jwtTokenProvider.isExpiredRefreshToken(REFRESH_TOKEN)).willReturn(false);
    given(authTokensGenerator.reissueAccessToken(anyLong(), any(String.class))).willReturn(
        newAuthTokens);

    // when
    AuthTokens renewedAuthTokens = authService.renew(Instancio.create(Long.class),
        REFRESH_TOKEN);

    // then
    assertAll(
        () -> assertThat(renewedAuthTokens.accessToken()).isNotEqualTo(authTokens.accessToken()),
        () -> assertThat(renewedAuthTokens.refreshToken()).isEqualTo(REFRESH_TOKEN)
    );
  }

  @DisplayName("refresh token이 만료될 경우 access token 을 갱신 시 예외를 던진다.")
  @Test
  void renewFailTest() {
    // given
    given(jwtTokenProvider.isExpiredRefreshToken(REFRESH_TOKEN)).willReturn(true);

    // when, then
    assertThatThrownBy(() -> authService.renew(Instancio.create(Long.class), REFRESH_TOKEN))
        .isInstanceOf(InvalidAuthException.class);
  }

  @DisplayName("로그아웃 시킬 수 있다.")
  @Test
  void logoutTest() {
    // given
    Long userId = Instancio.create(Long.class);
    willDoNothing().given(refreshTokenCommand).deleteRefreshToken(anyLong());

    // when, then
    assertThatCode(() -> authService.logout(userId))
        .doesNotThrowAnyException();
  }

}
