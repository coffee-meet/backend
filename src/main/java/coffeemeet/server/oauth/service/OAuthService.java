package coffeemeet.server.oauth.service;

import coffeemeet.server.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import coffeemeet.server.oauth.client.OAuthMemberClientComposite;
import coffeemeet.server.oauth.dto.OAuthInfoDto;
import coffeemeet.server.user.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

  private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
  private final OAuthMemberClientComposite oauthMemberClientComposite;

  public String getAuthCodeRequestUrl(OAuthProvider oAuthProvider) {
    return authCodeRequestUrlProviderComposite.provide(oAuthProvider);
  }

  public OAuthInfoDto.Response getOAuthInfo(OAuthProvider oAuthProvider, String authCode) {
    return oauthMemberClientComposite.fetch(oAuthProvider,
        authCode);
  }

}
