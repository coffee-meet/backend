package coffeemeet.server.oauth.infrastructure.kakao.dto;

import coffeemeet.server.oauth.dto.OAuthInfoDto;
import coffeemeet.server.user.domain.OAuthProvider;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record KakaoMemberResponse(
    Long id,
    KakaoAccount kakaoAccount
) {

  public OAuthInfoDto.Response toOAuthInfoResponse() {
    return OAuthInfoDto.Response.of(
        kakaoAccount.name,
        kakaoAccount.profile.profileImageUrl,
        kakaoAccount.birthyear,
        kakaoAccount.birthday,
        kakaoAccount.email,
        OAuthProvider.KAKAO,
        String.valueOf(id)
    );
  }

  @JsonNaming(SnakeCaseStrategy.class)
  private record KakaoAccount(
      Profile profile,
      String name,
      String email,
      String birthyear,
      String birthday
  ) {

  }

  @JsonNaming(SnakeCaseStrategy.class)
  private record Profile(
      String profileImageUrl
  ) {

  }

}
