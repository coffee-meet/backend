package coffeemeet.server.oauth.infrastructure.kakao.client;

import coffeemeet.server.oauth.infrastructure.kakao.config.KakaoProperties;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberResponse;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoTokens;
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
public class KakaoApiClient {

  private static final String REQUEST_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
  private static final String REQUEST_INFO_URL = "https://kapi.kakao.com/v2/user/me";
  private static final String GRANT_TYPE = "authorization_code";
  private static final String BEARER_TYPE = "Bearer ";

  private final RestTemplate restTemplate;
  private final KakaoProperties kakaoProperties;

  public KakaoTokens fetchToken(String authCode) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", GRANT_TYPE);
    body.add("client_id", kakaoProperties.getClientId());
    body.add("redirect_uri", kakaoProperties.getRedirectUrl());
    body.add("client_secret", kakaoProperties.getClientSecret());
    body.add("code", authCode);

    HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

    KakaoTokens response = restTemplate.postForObject(REQUEST_TOKEN_URL, request,
        KakaoTokens.class);

    assert response != null;
    return response;
  }

  public KakaoMemberResponse fetchMember(String accessToken) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.set("Authorization", BEARER_TYPE + accessToken);

    HttpEntity<?> request = new HttpEntity<>(httpHeaders);

    KakaoMemberResponse response = restTemplate.exchange(REQUEST_INFO_URL, HttpMethod.GET, request,
        KakaoMemberResponse.class).getBody();

    assert response != null;
    return response;
  }

}
