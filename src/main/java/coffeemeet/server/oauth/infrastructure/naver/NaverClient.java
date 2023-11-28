package coffeemeet.server.oauth.infrastructure.naver;

import static coffeemeet.server.oauth.utils.constant.OAuthConstant.AUTHORIZATION;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.AUTHORIZATION_CODE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.BEARER_TYPE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CLIENT_ID;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CLIENT_SECRET;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CODE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.GRANT_TYPE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.REDIRECT_URI;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.STATE;

import coffeemeet.server.oauth.infrastructure.naver.dto.NaverMemberDetail;
import coffeemeet.server.oauth.config.naver.NaverProperties;
import coffeemeet.server.oauth.infrastructure.naver.dto.NaverTokens;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverClient {

  private static final String REQUEST_TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
  private static final String REQUEST_INFO_URL = "https://openapi.naver.com/v1/nid/me";

  private final RestTemplate restTemplate;
  private final NaverProperties naverProperties;

  public NaverTokens fetchToken(String authCode) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add(GRANT_TYPE, AUTHORIZATION_CODE);
    body.add(CLIENT_ID, naverProperties.getClientId());
    body.add(REDIRECT_URI, naverProperties.getRedirectUrl());
    body.add(CLIENT_SECRET, naverProperties.getClientSecret());
    body.add(CODE, authCode);
    body.add(STATE, URLEncoder.encode(naverProperties.getRedirectUrl(), StandardCharsets.UTF_8));

    HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

    NaverTokens response = restTemplate.postForObject(REQUEST_TOKEN_URL, request,
        NaverTokens.class);

    assert response != null;
    return response;
  }

  public NaverMemberDetail fetchMember(String accessToken) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.set(AUTHORIZATION, BEARER_TYPE + accessToken);

    HttpEntity<?> request = new HttpEntity<>(httpHeaders);

    NaverMemberDetail response = restTemplate.exchange(REQUEST_INFO_URL, HttpMethod.GET, request,
        NaverMemberDetail.class).getBody();

    assert response != null;
    return response;
  }

}
