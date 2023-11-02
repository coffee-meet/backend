package coffeemeet.server.auth.implement;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.auth.infrastructure.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class RefreshTokenCommand {

  private final RefreshTokenRepository refreshTokenRepository;

  public void createRefreshToken(RefreshToken refreshToken) {
    refreshTokenRepository.save(refreshToken);
  }

  public void deleteRefreshToken(Long userId) {
    refreshTokenRepository.deleteById(userId);
  }

}
