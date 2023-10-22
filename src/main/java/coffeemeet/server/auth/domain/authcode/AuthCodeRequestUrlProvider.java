package coffeemeet.server.auth.domain.authcode;

import coffeemeet.server.user.domain.OAuthProvider;

public interface AuthCodeRequestUrlProvider {

  OAuthProvider oAuthProvider();

  String provide();

}
