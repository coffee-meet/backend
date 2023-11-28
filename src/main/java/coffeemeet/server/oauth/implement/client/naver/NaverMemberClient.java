package coffeemeet.server.oauth.implement.client.naver;

import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.oauth.implement.client.OAuthMemberClient;
import coffeemeet.server.oauth.infrastructure.naver.NaverClient;
import coffeemeet.server.oauth.infrastructure.naver.dto.NaverMemberDetail;
import coffeemeet.server.oauth.infrastructure.naver.dto.NaverTokens;
import coffeemeet.server.user.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverMemberClient implements OAuthMemberClient {

  private final NaverClient naverClient;

  @Override
  public OAuthProvider oAuthProvider() {
    return OAuthProvider.NAVER;
  }

  @Override
  public OAuthMemberDetail fetch(String authCode) {
    NaverTokens tokenInfo = naverClient.fetchToken(authCode);
    NaverMemberDetail response = naverClient.fetchMember(tokenInfo.accessToken());
    return response.toOAuthMemberDetail();
  }

}
