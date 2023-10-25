package coffeemeet.server.auth.service;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.auth.repository.RefreshTokenRepository;
import coffeemeet.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private static final String EXPIRED_REFRESH_TOKEN_MESSAGE = "리프레시 토큰이 만료되었습니다. 다시 로그인해 주세요.";

  private final UserService userService;
  private final AuthTokensGenerator authTokensGenerator;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  public AuthTokens renew(Long userId, String refreshToken) {
    if (jwtTokenProvider.isExpiredRefreshToken(refreshToken)) {
      throw new IllegalArgumentException(EXPIRED_REFRESH_TOKEN_MESSAGE);
    } else {
      return authTokensGenerator.reissueAccessToken(userId, refreshToken);
    }
  }

  public void logout(Long userId) {
    refreshTokenRepository.deleteById(userId);
  }

  @Transactional
  public void delete(Long userId) {
    userService.deleteUser(userId);
    refreshTokenRepository.deleteById(userId);
  }

}
