package coffeemeet.server.oauth.service;

import coffeemeet.server.oauth.infrastructure.OAuthUnlinkClientComposite;
import coffeemeet.server.user.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnlinkService {

  private final OAuthUnlinkClientComposite oAuthUnlinkClientComposite;

  public void unlink(OAuthProvider oAuthProvider, String accessToken){
    oAuthUnlinkClientComposite.unlink(oAuthProvider, accessToken);
  }

}
