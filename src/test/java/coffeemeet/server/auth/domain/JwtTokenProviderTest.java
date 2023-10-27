package coffeemeet.server.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

  private static final String SUBJECT = "123";
  private static final int ACCESS_TOKEN_EXPIRE_TIME = 3600000;

  private final String secretKey = Base64.getEncoder()
      .encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());

  private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey);

  @DisplayName("jwt 를 생성할 수 있다.")
  @Test
  void generateTest() {
    // given
    Date date = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME);

    // when
    String result = jwtTokenProvider.generate(SUBJECT, date);

    // then
    assertThat(result).isNotNull();
  }

  @DisplayName("jwt 로부터 userId 를 가져올 수 있다.")
  @Test
  void extractUserIdTest() {
    // given
    Date date = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME);

    // when
    String token = jwtTokenProvider.generate(SUBJECT, date);
    Long result = jwtTokenProvider.extractUserId(token);

    // then
    assertThat(result).isEqualTo(Long.parseLong(SUBJECT));
  }

  @DisplayName("jwt(refresh token) 로부터 토큰이 만료되었는지 확인할 수 있다.")
  @Test
  void isExpiredRefreshTokenTest() {
    // given
    Date date = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME);

    String token = jwtTokenProvider.generate(SUBJECT, date);

    // when, then
    assertThat(jwtTokenProvider.isExpiredRefreshToken(token)).isFalse();
  }

}
