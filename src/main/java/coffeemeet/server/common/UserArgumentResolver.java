package coffeemeet.server.common;

import static coffeemeet.server.auth.exception.AuthErrorCode.*;

import coffeemeet.server.auth.domain.JwtTokenProvider;
import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.auth.exception.AuthErrorCode;
import coffeemeet.server.auth.repository.RefreshTokenRepository;
import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.user.dto.AuthInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String USER_AUTHENTICATION_FAILED_MESSAGE = "사용자(%s)의 재인증(로그인)이 필요합니다.";
  private static final String HEADER_AUTHENTICATION_FAILED_MESSAGE = "(%s)는 잘못된 권한 헤더입니다.";

  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(AuthInfo.class) && parameter.hasParameterAnnotation(
        Login.class);
  }

  @Override
  public AuthInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
    String authHeader = httpServletRequest.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      Long userId = jwtTokenProvider.extractUserId(token);
      RefreshToken refreshToken = getRefreshToken(userId);
      return new AuthInfo(userId, refreshToken.getValue());
    }
    throw new InvalidInputException(
        AUTHENTICATION_FAILED,
        String.format(HEADER_AUTHENTICATION_FAILED_MESSAGE, authHeader)
    );
  }

  private RefreshToken getRefreshToken(Long userId) {
    return refreshTokenRepository.findById(userId)
        .orElseThrow(() -> new InvalidInputException(
            AUTHENTICATION_FAILED,
            String.format(USER_AUTHENTICATION_FAILED_MESSAGE, userId)));
  }

}
