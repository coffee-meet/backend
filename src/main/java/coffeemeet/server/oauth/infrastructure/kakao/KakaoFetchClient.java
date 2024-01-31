package coffeemeet.server.oauth.infrastructure.kakao;

import static coffeemeet.server.oauth.utils.constant.OAuthConstant.AUTHORIZATION;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.AUTHORIZATION_CODE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.BEARER_TYPE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CLIENT_ID;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CLIENT_SECRET;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CODE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.GRANT_TYPE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.REDIRECT_URI;

import coffeemeet.server.oauth.config.kakao.KakaoProperties;
import coffeemeet.server.oauth.infrastructure.kakao.dto.KakaoMemberDetail;
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
public class KakaoFetchClient {

  private static final String REQUEST_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
  private static final String REQUEST_INFO_URL = "https://kapi.kakao.com/v2/user/me";

  private final RestTemplate restTemplate;
  private final KakaoProperties kakaoProperties;

  public KakaoTokens fetchToken(String authCode) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add(GRANT_TYPE, AUTHORIZATION_CODE);
    body.add(CLIENT_ID, kakaoProperties.getClientId());
    body.add(REDIRECT_URI, kakaoProperties.getRedirectUrl());
    body.add(CLIENT_SECRET, kakaoProperties.getClientSecret());
    body.add(CODE, authCode);

    HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

    KakaoTokens response = restTemplate.postForObject(REQUEST_TOKEN_URL, request,
        KakaoTokens.class);

    assert response != null;
    return response;
  }

  public KakaoMemberDetail fetchMember(String accessToken) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.set(AUTHORIZATION, BEARER_TYPE + accessToken);

    HttpEntity<?> request = new HttpEntity<>(httpHeaders);
    KakaoMemberDetail response = restTemplate.exchange(REQUEST_INFO_URL, HttpMethod.GET, request,
        KakaoMemberDetail.class).getBody();

    assert response != null;
    return response;
  }

}
