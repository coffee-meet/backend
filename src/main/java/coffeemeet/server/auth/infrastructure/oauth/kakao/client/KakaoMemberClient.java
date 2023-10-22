package coffeemeet.server.auth.infrastructure.oauth.kakao.client;

import coffeemeet.server.auth.domain.client.OAuthMemberClient;
import coffeemeet.server.auth.dto.OAuthInfoResponse;
import coffeemeet.server.auth.infrastructure.oauth.kakao.dto.KakaoMemberResponse;
import coffeemeet.server.auth.infrastructure.oauth.kakao.dto.KakaoTokens;
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
