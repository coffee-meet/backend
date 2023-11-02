package coffeemeet.server.oauth.implement.provider;

import coffeemeet.server.user.domain.OAuthProvider;

public interface AuthCodeRequestUrlProvider {

  OAuthProvider oAuthProvider();

  String provide();

}
