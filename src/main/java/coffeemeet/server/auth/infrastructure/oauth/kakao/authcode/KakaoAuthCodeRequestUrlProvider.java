package coffeemeet.server.auth.infrastructure.oauth.kakao.authcode;

import coffeemeet.server.auth.infrastructure.oauth.kakao.config.KakaoProperties;
import coffeemeet.server.auth.domain.authcode.AuthCodeRequestUrlProvider;
import coffeemeet.server.user.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private static final String AUTHORIZE_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String RESPONSE_TYPE = "code";

    private final KakaoProperties kakaoProperties;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString(AUTHORIZE_URL)
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUrl())
                .toUriString();
    }

}
