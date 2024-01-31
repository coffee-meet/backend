package coffeemeet.server.auth.service;

import static coffeemeet.server.auth.exception.AuthErrorCode.EXPIRED_TOKEN;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.auth.implement.RefreshTokenCommand;
import coffeemeet.server.common.execption.InvalidAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private static final String EXPIRED_REFRESH_TOKEN_MESSAGE = "리프레시 토큰(%s)이 만료되었습니다. 다시 로그인해 주세요.";

  private final AuthTokensGenerator authTokensGenerator;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenCommand refreshTokenCommand;

  public AuthTokens renew(Long userId, String refreshToken) {
    if (jwtTokenProvider.isExpiredRefreshToken(refreshToken)) {
      throw new InvalidAuthException(
          EXPIRED_TOKEN,
          String.format(EXPIRED_REFRESH_TOKEN_MESSAGE, refreshToken));
    } else {
      return authTokensGenerator.reissueAccessToken(userId, refreshToken);
    }
  }

  public void logout(Long userId) {
    refreshTokenCommand.deleteRefreshToken(userId);
  }

}
