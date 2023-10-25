package coffeemeet.server.auth.domain;

import coffeemeet.server.auth.repository.RefreshTokenRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthTokensGenerator {

  private static final String BEARER_TYPE = "Bearer ";

  private final JwtTokenProvider jwtTokenProvider;
  private final Long accessTokenExpireTime;
  private final Long refreshTokenExpireTime;
  private final RefreshTokenRepository refreshTokenRepository;

  public AuthTokensGenerator(JwtTokenProvider jwtTokenProvider,
      @Value("${jwt.access-token-expire-time}") Long accessTokenExpireTime,
      @Value("${jwt.refresh-token-expire-time}") Long refreshTokenExpireTime,
      RefreshTokenRepository refreshTokenRepository) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.accessTokenExpireTime = accessTokenExpireTime;
    this.refreshTokenExpireTime = refreshTokenExpireTime;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  public AuthTokens generate(Long userId) {
    long now = (new Date()).getTime();
    Date accessTokenExpiredAt = new Date(now + accessTokenExpireTime);
    Date refreshTokenExpiredAt = new Date(now + refreshTokenExpireTime);

    String subject = userId.toString();
    String accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);
    String refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiredAt);

    RefreshToken refreshTokenEntity = RefreshToken.builder()
        .userId(userId)
        .value(refreshToken)
        .build();
    refreshTokenRepository.save(refreshTokenEntity);

    return AuthTokens.of(
        BEARER_TYPE + accessToken,
        BEARER_TYPE + refreshToken
    );
  }

  public AuthTokens reissueAccessToken(Long userId, String refreshToken) {
    long now = (new Date()).getTime();
    Date accessTokenExpiredAt = new Date(now + accessTokenExpireTime);

    String subject = userId.toString();
    String accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);

    return AuthTokens.of(
        BEARER_TYPE + accessToken,
        BEARER_TYPE + refreshToken
    );
  }

}
