package coffeemeet.server.oauth.implement.client;

import coffeemeet.server.oauth.infrastructure.OAuthUnlinkDetail;
import coffeemeet.server.user.domain.OAuthProvider;

public interface OAuthUnlinkClient {

  OAuthProvider oAuthProvider();

  OAuthUnlinkDetail unlink(String accessToken);

}
