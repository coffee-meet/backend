package coffeemeet.server.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.auth.repository.RefreshTokenRepository;
import coffeemeet.server.common.fixture.dto.AuthTokensFixture;
import coffeemeet.server.user.service.UserService;
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

  @Mock
  private UserService userService;

  @Mock
  private AuthTokensGenerator authTokensGenerator;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @Mock
  private RefreshTokenRepository refreshTokenRepository;

  @DisplayName("access token 을 갱신할 수 있다.")
  @Test
  void renewTest() {
    // given
    AuthTokens authTokens = AuthTokensFixture.authTokens();
    AuthTokens newAuthTokens = AuthTokensFixture.authTokens(REFRESH_TOKEN);

    given(jwtTokenProvider.isExpiredRefreshToken(REFRESH_TOKEN)).willReturn(false);
    given(authTokensGenerator.reissueAccessToken(anyLong(), any(String.class))).willReturn(
        newAuthTokens);

    // when
    AuthTokens renewedAuthTokens = authService.renew((long) Math.random(), REFRESH_TOKEN);

    // then
    assertAll(
        () -> assertThat(renewedAuthTokens.accessToken()).isNotEqualTo(authTokens.accessToken()),
        () -> assertThat(renewedAuthTokens.refreshToken()).isEqualTo(REFRESH_TOKEN)
    );
  }

  @DisplayName("로그아웃 시킬 수 있다.")
  @Test
  void logoutTest() {
    // given
    willDoNothing().given(refreshTokenRepository).deleteById(anyLong());

    // when
    authService.logout((long) Math.random());

    // then
    assertThat(true).isTrue();
  }

  @DisplayName("회원 탈퇴 시킬 수 있다.")
  @Test
  void deleteTest() {
    // given
    willDoNothing().given(refreshTokenRepository).deleteById(anyLong());
    willDoNothing().given(userService).deleteUser(anyLong());

    // when
    authService.delete((long) Math.random());

    // then
    assertThat(true).isTrue();
  }

}
