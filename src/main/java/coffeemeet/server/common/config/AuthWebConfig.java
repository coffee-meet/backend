package coffeemeet.server.common.config;

import coffeemeet.server.auth.infrastructure.RefreshTokenRepository;
import coffeemeet.server.auth.utils.JwtTokenProvider;
import coffeemeet.server.auth.utils.converter.OAuthProviderConverter;
import coffeemeet.server.common.UserArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthWebConfig implements WebMvcConfigurer {

  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new OAuthProviderConverter());
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new UserArgumentResolver(jwtTokenProvider, refreshTokenRepository));
  }

}
