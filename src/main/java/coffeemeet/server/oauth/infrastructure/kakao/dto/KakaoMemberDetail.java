package coffeemeet.server.oauth.infrastructure.kakao.dto;

import static coffeemeet.server.oauth.utils.constant.OAuthConstant.DEFAULT_IMAGE_URL;

import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.user.domain.OAuthProvider;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;

@JsonNaming(SnakeCaseStrategy.class)
public record KakaoMemberDetail(
    Long id,
    KakaoAccount kakaoAccount
) {

  public OAuthMemberDetail toOAuthMemberDetail() {
    String profileImageUrl =
        kakaoAccount.profile != null ? kakaoAccount.profile.profileImageUrl : null;

    return OAuthMemberDetail.of(
        getProfileImageOrDefault(profileImageUrl),
        kakaoAccount.email,
        OAuthProvider.KAKAO,
        String.valueOf(id)
    );
  }

  private String getProfileImageOrDefault(String profileImageUrl) {
    return profileImageUrl == null ? DEFAULT_IMAGE_URL : profileImageUrl;
  }

  @JsonNaming(SnakeCaseStrategy.class)
  private record KakaoAccount(
      Profile profile,
      String email
  ) {

  }

  @JsonNaming(SnakeCaseStrategy.class)
  private record Profile(
      @Nullable
      String profileImageUrl
  ) {

  }

}
