package coffeemeet.server.oauth.client;

import coffeemeet.server.oauth.dto.OAuthInfoDto;
import coffeemeet.server.user.domain.OAuthProvider;

public interface OAuthMemberClient {

  OAuthProvider oAuthProvider();

  OAuthInfoDto.Response fetch(String authCode);

}
