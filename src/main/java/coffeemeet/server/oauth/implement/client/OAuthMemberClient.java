package coffeemeet.server.oauth.implement.client;

import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.user.domain.OAuthProvider;

public interface OAuthMemberClient {

  OAuthProvider oAuthProvider();

  OAuthMemberDetail fetch(String authCode);

}
