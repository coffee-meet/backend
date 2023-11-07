package coffeemeet.server.oauth.infrastructure.kakao.dto;

import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.user.domain.OAuthProvider;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record KakaoMemberDetail(
    Long id,
    KakaoAccount kakaoAccount
) {

  public OAuthMemberDetail toOAuthMemberDetail() {
    return OAuthMemberDetail.of(
        kakaoAccount.profile.profileImageUrl,
        kakaoAccount.email,
        OAuthProvider.KAKAO,
        String.valueOf(id)
    );
  }

  @JsonNaming(SnakeCaseStrategy.class)
  private record KakaoAccount(
      Profile profile,
      String email
  ) {

  }

  @JsonNaming(SnakeCaseStrategy.class)
  private record Profile(
      String profileImageUrl
  ) {

  }

}
