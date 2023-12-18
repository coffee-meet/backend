package coffeemeet.server.auth.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.auth.implement.RefreshTokenCommand;
import com.github.javafaker.Faker;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthTokensGeneratorTest {

  private static final long accessTokenExpireTime = 1800;
  private static final long refreshTokenExpireTime = 3600;
  private static final String BEARER_TYPE = "Bearer ";
  private static final String ACCESS_TOKEN = "accessToken";
  private static final String REFRESH_TOKEN = "refreshToken";

  @InjectMocks
  private AuthTokensGenerator authTokensGenerator;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @Mock
  private RefreshTokenCommand refreshTokenCommand;

  @BeforeEach
  public void init() {
    authTokensGenerator = new AuthTokensGenerator(jwtTokenProvider, accessTokenExpireTime,
        refreshTokenExpireTime, refreshTokenCommand);
  }

  @DisplayName("access token & refresh token 발급할 수 있다.")
  @Test
  void generateTest() {
    // given
    given(jwtTokenProvider.generate(anyString(), any(Date.class))).willReturn(ACCESS_TOKEN,
        REFRESH_TOKEN);

    // when
    AuthTokens authTokens = authTokensGenerator.generate(Faker.instance().random().nextLong());

    // then
    assertAll(
        () -> assertThat(authTokens.accessToken()).isEqualTo(BEARER_TYPE + ACCESS_TOKEN),
        () -> assertThat(authTokens.refreshToken()).isEqualTo(BEARER_TYPE + REFRESH_TOKEN)
    );
  }

  @DisplayName("access token 을 갱신 할 수 있다.")
  @Test
  void reissueAccessTokenTest() {
    // given
    given(jwtTokenProvider.generate(anyString(), any(Date.class))).willReturn(ACCESS_TOKEN,
        REFRESH_TOKEN);

    // when
    AuthTokens authTokens = authTokensGenerator.reissueAccessToken(
        Faker.instance().random().nextLong(),
        REFRESH_TOKEN);

    // then
    assertAll(
        () -> assertThat(authTokens.accessToken()).isEqualTo(BEARER_TYPE + ACCESS_TOKEN),
        () -> assertThat(authTokens.refreshToken()).isEqualTo(BEARER_TYPE + REFRESH_TOKEN)
    );
  }

}
