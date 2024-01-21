package coffeemeet.server.oauth.service;

import coffeemeet.server.auth.implement.RefreshTokenCommand;
import coffeemeet.server.oauth.implement.provider.AuthCodeRequestUrlProviderRegistry;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

  private final AuthCodeRequestUrlProviderRegistry authCodeRequestUrlProviderRegistry;
  private final UserService userService;
  private final RefreshTokenCommand refreshTokenCommand;

  public String getAuthCodeRequestUrl(OAuthProvider oAuthProvider) {
    return authCodeRequestUrlProviderRegistry.provide(oAuthProvider);
  }

  @Transactional
  public void delete(Long userId, String token, OAuthProvider oAuthProvider) {
    userService.deleteUser(userId, token, oAuthProvider);
    refreshTokenCommand.deleteRefreshToken(userId);
  }

}
