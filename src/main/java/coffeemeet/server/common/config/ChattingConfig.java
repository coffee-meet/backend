package coffeemeet.server.common.config;

import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.auth.implement.RefreshTokenQuery;
import coffeemeet.server.common.presentation.interceptor.StompInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class ChattingConfig implements WebSocketMessageBrokerConfigurer {

  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenQuery refreshTokenQuery;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/stomp").setAllowedOriginPatterns("*");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/sub");
    registry.setApplicationDestinationPrefixes("/pub");
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new StompInterceptor(jwtTokenProvider, refreshTokenQuery));
  }

}
