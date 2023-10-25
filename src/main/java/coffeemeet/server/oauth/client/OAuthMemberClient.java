package coffeemeet.server.oauth.client;

import coffeemeet.server.oauth.dto.OAuthInfoResponse;
import coffeemeet.server.user.domain.OAuthProvider;

public interface OAuthMemberClient {

  OAuthProvider oAuthProvider();

  OAuthInfoResponse fetch(String authCode);

}
