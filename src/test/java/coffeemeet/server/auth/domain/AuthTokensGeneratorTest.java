package coffeemeet.server.auth.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import coffeemeet.server.auth.repository.RefreshTokenRepository;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthTokensGeneratorTest {

  private static final Long accessTokenExpireTime = 1800L;
  private static final Long refreshTokenExpireTime = 3600L;
  private static final String BEARER_TYPE = "Bearer ";
  private static final String ACCESS_TOKEN = "accessToken";
  private static final String REFRESH_TOKEN = "refreshToken";

  @InjectMocks
  private AuthTokensGenerator authTokensGenerator;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @Mock
  private RefreshTokenRepository refreshTokenRepository;

  @BeforeEach
  public void init() {
    authTokensGenerator = new AuthTokensGenerator(jwtTokenProvider, accessTokenExpireTime,
        refreshTokenExpireTime, refreshTokenRepository);
  }

  @Test
  void generateTest() {
    // given
    when(jwtTokenProvider.generate(anyString(), any(Date.class))).thenReturn(ACCESS_TOKEN,
        REFRESH_TOKEN);
    AuthTokens authTokens = authTokensGenerator.generate((long) Math.random());

    assertThat(authTokens.accessToken()).isEqualTo(BEARER_TYPE + ACCESS_TOKEN);
    assertThat(authTokens.refreshToken()).isEqualTo(BEARER_TYPE + REFRESH_TOKEN);
  }

}
