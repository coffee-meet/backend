package coffeemeet.server.oauth.implement.provider.kakao;

import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CLIENT_ID;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CODE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.REDIRECT_URI;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.RESPONSE_TYPE;

import coffeemeet.server.oauth.config.kakao.KakaoProperties;
import coffeemeet.server.oauth.implement.provider.AuthCodeRequestUrlProvider;
import coffeemeet.server.user.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

  private static final String AUTHORIZE_URL = "https://kauth.kakao.com/oauth/authorize";

  private final KakaoProperties kakaoProperties;

  @Override
  public OAuthProvider oAuthProvider() {
    return OAuthProvider.KAKAO;
  }

  @Override
  public String provide() {
    return UriComponentsBuilder
        .fromUriString(AUTHORIZE_URL)
        .queryParam(RESPONSE_TYPE, CODE)
        .queryParam(CLIENT_ID, kakaoProperties.getClientId())
        .queryParam(REDIRECT_URI, kakaoProperties.getRedirectUrl())
        .toUriString();
  }

}
