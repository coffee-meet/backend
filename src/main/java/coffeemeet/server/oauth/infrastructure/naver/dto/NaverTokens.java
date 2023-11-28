package coffeemeet.server.oauth.infrastructure.naver.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record NaverTokens(
    String accessToken,
    String refreshToken,
    String tokenType,
    int expiresIn,
    String error,
    String error_description
) {

}

