package coffeemeet.server.oauth.infrastructure.naver;

import static coffeemeet.server.oauth.utils.constant.OAuthConstant.AUTHORIZATION;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.AUTHORIZATION_CODE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.BEARER_TYPE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CLIENT_ID;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CLIENT_SECRET;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.GRANT_TYPE;

import coffeemeet.server.oauth.config.naver.NaverProperties;
import coffeemeet.server.oauth.infrastructure.OAuthUnlinkClient;
import coffeemeet.server.oauth.infrastructure.OAuthUnlinkDetail;
import coffeemeet.server.oauth.infrastructure.naver.dto.NaverUnlinkDetail;
import coffeemeet.server.user.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverUnlinkClient implements OAuthUnlinkClient {

  private static final String UNLINK_USER_URL = "https://nid.naver.com/oauth2.0/token";

  private final RestTemplate restTemplate;
  private final NaverProperties naverProperties;

  @Override
  public OAuthProvider oAuthProvider() {
    return OAuthProvider.NAVER;
  }

  public OAuthUnlinkDetail unlink(String accessToken) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    httpHeaders.set(AUTHORIZATION, BEARER_TYPE + accessToken);
    httpHeaders.set(CLIENT_ID, naverProperties.getClientId());
    httpHeaders.set(CLIENT_SECRET, naverProperties.getClientSecret());
    httpHeaders.set(GRANT_TYPE, AUTHORIZATION_CODE);

    HttpEntity<?> request = new HttpEntity<>(httpHeaders);

    NaverUnlinkDetail response = restTemplate.exchange(UNLINK_USER_URL, HttpMethod.POST, request,
        NaverUnlinkDetail.class).getBody();

    assert response != null;
    return response.toOAuthUnlinkDetail();
  }

}
