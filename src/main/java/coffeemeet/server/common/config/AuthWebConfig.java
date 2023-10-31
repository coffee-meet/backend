package coffeemeet.server.common.config;

import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.auth.repository.RefreshTokenRepository;
import coffeemeet.server.auth.service.cq.RefreshTokenQuery;
import coffeemeet.server.common.UserArgumentResolver;
import coffeemeet.server.oauth.utils.converter.OAuthProviderConverter;
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
  private final RefreshTokenQuery refreshTokenQuery;

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new OAuthProviderConverter());
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new UserArgumentResolver(jwtTokenProvider, refreshTokenQuery));
  }

}
