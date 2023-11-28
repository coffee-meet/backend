package coffeemeet.server.oauth.config.naver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth.naver")
public class NaverProperties {

  private final String clientId;
  private final String redirectUrl;
  private final String clientSecret;

}
