package coffeemeet.server.common.config;

import coffeemeet.server.auth.utils.JwtTokenProvider;
import coffeemeet.server.auth.utils.converter.OAuthProviderConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthWebConfig implements WebMvcConfigurer {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new OAuthProviderConverter());
  }

}
