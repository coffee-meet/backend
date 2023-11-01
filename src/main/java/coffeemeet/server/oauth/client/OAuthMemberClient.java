package coffeemeet.server.oauth.client;

import coffeemeet.server.oauth.dto.OAuthUserInfoDto;
import coffeemeet.server.user.domain.OAuthProvider;

public interface OAuthMemberClient {

  OAuthProvider oAuthProvider();

  OAuthUserInfoDto.Response fetch(String authCode);

}
