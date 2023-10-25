package coffeemeet.server.oauth.infrastructure.kakao.client;

import coffeemeet.server.oauth.client.OAuthMemberClient;
import coffeemeet.server.oauth.dto.OAuthInfoResponse;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberResponse;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoTokens;
import coffeemeet.server.user.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OAuthMemberClient {

  private final KakaoApiClient kakaoApiClient;

  @Override
  public OAuthProvider oAuthProvider() {
    return OAuthProvider.KAKAO;
  }

  @Override
  public OAuthInfoResponse fetch(String authCode) {
    KakaoTokens tokenInfo = kakaoApiClient.fetchToken(authCode);
    KakaoMemberResponse response = kakaoApiClient.fetchMember(tokenInfo.accessToken());

    return response.toOAuthInfoResponse();
  }

}
