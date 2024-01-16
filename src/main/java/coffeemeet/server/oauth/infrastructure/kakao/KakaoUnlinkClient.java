package coffeemeet.server.oauth.infrastructure.kakao;

import static coffeemeet.server.oauth.utils.constant.OAuthConstant.AUTHORIZATION;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.BEARER_TYPE;

import coffeemeet.server.oauth.infrastructure.OAuthUnlinkClient;
import coffeemeet.server.oauth.infrastructure.OAuthUnlinkDetail;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoUnlinkDetail;
import coffeemeet.server.user.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoUnlinkClient implements OAuthUnlinkClient {

  private static final String UNLINK_USER_URL = "https://kapi.kakao.com/v1/user/unlink";

  private final RestTemplate restTemplate;

  @Override
  public OAuthProvider oAuthProvider() {
    return OAuthProvider.KAKAO;
  }

  public OAuthUnlinkDetail unlink(String accessToken) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.set(AUTHORIZATION, BEARER_TYPE + accessToken);

    HttpEntity<?> request = new HttpEntity<>(httpHeaders);

    KakaoUnlinkDetail response = restTemplate.exchange(UNLINK_USER_URL, HttpMethod.POST, request,
        KakaoUnlinkDetail.class).getBody();

    assert response != null;
    return response.toOAuthUnlinkDetail();
  }

}
