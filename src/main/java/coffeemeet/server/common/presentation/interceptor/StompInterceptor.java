package coffeemeet.server.common.presentation.interceptor;

import static coffeemeet.server.auth.exception.AuthErrorCode.AUTHENTICATION_FAILED;
import static coffeemeet.server.common.execption.GlobalErrorCode.STOMP_ACCESSOR_NOT_FOUND;

import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.auth.implement.RefreshTokenQuery;
import coffeemeet.server.common.execption.InvalidAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

  private static final String STOMP_ACCESSOR_NOT_FOUND_MESSAGE = "stomp header accessor를 찾을 수 없습니다.";
  private static final String HEADER_AUTHENTICATION_FAILED_MESSAGE = "(%s)는 잘못된 권한 헤더입니다.";

  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenQuery refreshTokenQuery;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message,
        StompHeaderAccessor.class);
    if (headerAccessor == null) {
      throw new InvalidAuthException(
          STOMP_ACCESSOR_NOT_FOUND,
          STOMP_ACCESSOR_NOT_FOUND_MESSAGE
      );
    }

    if (headerAccessor.getCommand() == StompCommand.CONNECT) {
      String authHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
      if (authHeader != null && authHeader.startsWith("Bearer ", 1)) {
        String token = authHeader.substring(7, authHeader.length() - 1);
        Long userId = jwtTokenProvider.extractUserId(token);
        refreshTokenQuery.getRefreshToken(userId);
        headerAccessor.addNativeHeader("userId", String.valueOf(userId));
      } else {
        throw new InvalidAuthException(
            AUTHENTICATION_FAILED,
            String.format(HEADER_AUTHENTICATION_FAILED_MESSAGE, authHeader)
        );
      }
    }
    return message;
  }

}
