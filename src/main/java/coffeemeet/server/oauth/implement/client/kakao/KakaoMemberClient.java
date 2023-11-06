package coffeemeet.server.oauth.implement.client.kakao;

import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.oauth.implement.client.OAuthMemberClient;
import coffeemeet.server.oauth.infrastructure.kakao.KakaoClient;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberDetail;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoTokens;
import coffeemeet.server.user.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OAuthMemberClient {

  private final KakaoClient kakaoClient;

  @Override
  public OAuthProvider oAuthProvider() {
    return OAuthProvider.KAKAO;
  }

  @Override
  public OAuthMemberDetail fetch(String authCode) {
    KakaoTokens tokenInfo = kakaoClient.fetchToken(authCode);
    KakaoMemberDetail response = kakaoClient.fetchMember(tokenInfo.accessToken());

    return response.toOAuthMemberDetail();
  }

}
