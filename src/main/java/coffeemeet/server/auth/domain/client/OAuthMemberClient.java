package coffeemeet.server.auth.domain.client;

import coffeemeet.server.auth.dto.OAuthInfoResponse;
import coffeemeet.server.user.domain.OAuthProvider;

public interface OAuthMemberClient {

  OAuthProvider oAuthProvider();

  OAuthInfoResponse fetch(String authCode);

}
