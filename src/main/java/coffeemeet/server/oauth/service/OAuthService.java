package coffeemeet.server.oauth.service;

import coffeemeet.server.oauth.implement.provider.AuthCodeRequestUrlProviderComposite;
import coffeemeet.server.user.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

  private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;

  public String getAuthCodeRequestUrl(OAuthProvider oAuthProvider) {
    return authCodeRequestUrlProviderComposite.provide(oAuthProvider);
  }

}
