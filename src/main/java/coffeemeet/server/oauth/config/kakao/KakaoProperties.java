package coffeemeet.server.oauth.config.kakao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth.kakao")
public class KakaoProperties {

  private final String clientId;
  private final String redirectUrl;
  private final String clientSecret;

}
