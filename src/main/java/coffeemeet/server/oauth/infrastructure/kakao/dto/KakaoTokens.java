package coffeemeet.server.oauth.infrastructure.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record KakaoTokens(
    String accessToken,
    String tokenType,
    String refreshToken,
    int expiresIn,
    int refreshTokenExpiresIn
) {

}
