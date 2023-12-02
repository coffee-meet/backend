package coffeemeet.server.oauth.implement.provider.naver;

import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CLIENT_ID;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.CODE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.REDIRECT_URI;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.RESPONSE_TYPE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.STATE;

import coffeemeet.server.oauth.config.naver.NaverProperties;
import coffeemeet.server.oauth.implement.provider.AuthCodeRequestUrlProvider;
import coffeemeet.server.user.domain.OAuthProvider;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NaverAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

  private static final String AUTHORIZE_URL = "https://nid.naver.com/oauth2.0/authorize";

  private final NaverProperties naverProperties;

  @Override
  public OAuthProvider oAuthProvider() {
    return OAuthProvider.NAVER;
  }

  @Override
  public String provide() {
    return UriComponentsBuilder
        .fromUriString(AUTHORIZE_URL)
        .queryParam(RESPONSE_TYPE, CODE)
        .queryParam(CLIENT_ID, naverProperties.getClientId())
        .queryParam(REDIRECT_URI, naverProperties.getRedirectUrl())
        .queryParam(STATE, URLEncoder.encode(naverProperties.getRedirectUrl(),
            StandardCharsets.UTF_8))
        .toUriString();
  }

}
