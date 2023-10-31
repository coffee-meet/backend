package coffeemeet.server.auth.service;

import static coffeemeet.server.auth.exception.AuthErrorCode.AUTHENTICATION_FAILED;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.auth.service.cq.RefreshTokenCommand;
import coffeemeet.server.common.execption.InvalidAuthException;
import coffeemeet.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private static final String EXPIRED_REFRESH_TOKEN_MESSAGE = "리프레시 토큰(%s)이 만료되었습니다. 다시 로그인해 주세요.";

  private final UserService userService;
  private final AuthTokensGenerator authTokensGenerator;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenCommand refreshTokenCommand;

  public AuthTokens renew(Long userId, String refreshToken) {
    if (jwtTokenProvider.isExpiredRefreshToken(refreshToken)) {
      throw new InvalidAuthException(
          AUTHENTICATION_FAILED,
          String.format(EXPIRED_REFRESH_TOKEN_MESSAGE, refreshToken));
    } else {
      return authTokensGenerator.reissueAccessToken(userId, refreshToken);
    }
  }

  public void logout(Long userId) {
    refreshTokenCommand.deleteRefreshToken(userId);
  }

  @Transactional
  public void delete(Long userId) {
    userService.deleteUser(userId);
    refreshTokenCommand.deleteRefreshToken(userId);
  }

}
