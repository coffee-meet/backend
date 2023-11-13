package coffeemeet.server.common.presentation.interceptor;

import static coffeemeet.server.auth.exception.AuthErrorCode.AUTHENTICATION_FAILED;

import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.common.execption.InvalidAuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

  private static final String HEADER_AUTHENTICATION_FAILED_MESSAGE = "(%s)는 잘못된 권한 헤더입니다.";

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      jwtTokenProvider.extractUserId(token);
      return true;
    }
    throw new InvalidAuthException(
        AUTHENTICATION_FAILED,
        String.format(HEADER_AUTHENTICATION_FAILED_MESSAGE, authHeader)
    );
  }

}
