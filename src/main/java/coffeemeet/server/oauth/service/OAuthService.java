package coffeemeet.server.oauth.service;

import coffeemeet.server.auth.implement.RefreshTokenCommand;
import coffeemeet.server.oauth.implement.client.OAuthMemberUnlinkRegistry;
import coffeemeet.server.oauth.implement.provider.AuthCodeRequestUrlProviderRegistry;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.implement.UserCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

  private final AuthCodeRequestUrlProviderRegistry authCodeRequestUrlProviderRegistry;
  private final OAuthMemberUnlinkRegistry oAuthMemberUnlinkRegistry;
  private final UserCommand userCommand;
  private final RefreshTokenCommand refreshTokenCommand;

  public String getAuthCodeRequestUrl(OAuthProvider oAuthProvider) {
    return authCodeRequestUrlProviderRegistry.provide(oAuthProvider);
  }

  @Transactional
  public void unlink(Long userId, String accessToken, OAuthProvider oAuthProvider) {
    oAuthMemberUnlinkRegistry.unlink(oAuthProvider, accessToken);
    userCommand.deleteUser(userId);
    refreshTokenCommand.deleteRefreshToken(userId);
  }

}
