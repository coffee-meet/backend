package coffeemeet.server.oauth.infrastructure;

import coffeemeet.server.user.domain.OAuthProvider;

public interface OAuthUnlinkClient {

  OAuthProvider oAuthProvider();

  OAuthUnlinkDetail unlink(String accessToken);

}
