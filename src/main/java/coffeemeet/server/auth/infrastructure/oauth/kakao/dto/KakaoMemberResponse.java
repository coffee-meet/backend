package coffeemeet.server.auth.infrastructure.oauth.kakao.dto;

import coffeemeet.server.auth.domain.dto.OAuthInfoResponse;
import coffeemeet.server.user.domain.Birth;
import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.OAuthProvider;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record KakaoMemberResponse(
    Long id,
    KakaoAccount kakaoAccount
) {

  public OAuthInfoResponse toOAuthInfoResponse() {
    return OAuthInfoResponse.of(
        kakaoAccount.profile.name,
        kakaoAccount.profile.profileImageUrl,
        new Birth(kakaoAccount.birthyear, kakaoAccount.birthday),
        new Email(kakaoAccount.email),
        OAuthProvider.KAKAO,
        String.valueOf(id)
    );
  }

  @JsonNaming(SnakeCaseStrategy.class)
  private record KakaoAccount(
      Profile profile,
      String email,
      String birthyear,
      String birthday
  ) {

  }

  @JsonNaming(SnakeCaseStrategy.class)
  private record Profile(
      String name,
      String profileImageUrl
  ) {

  }

}
