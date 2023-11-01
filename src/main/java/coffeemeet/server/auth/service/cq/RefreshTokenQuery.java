package coffeemeet.server.auth.service.cq;

import static coffeemeet.server.auth.exception.AuthErrorCode.AUTHENTICATION_FAILED;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.auth.repository.RefreshTokenRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenQuery {

  private static final String USER_AUTHENTICATION_FAILED_MESSAGE = "사용자(%s)의 재인증(로그인)이 필요합니다.";

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshToken getRefreshToken(Long userId) {
    return refreshTokenRepository.findById(userId)
        .orElseThrow(() -> new InvalidInputException(
            AUTHENTICATION_FAILED,
            String.format(USER_AUTHENTICATION_FAILED_MESSAGE, userId)));
  }

}
