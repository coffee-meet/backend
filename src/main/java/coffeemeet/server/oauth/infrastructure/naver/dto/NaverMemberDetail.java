package coffeemeet.server.oauth.infrastructure.naver.dto;

import static coffeemeet.server.oauth.utils.constant.OAuthConstant.DEFAULT_IMAGE_URL;

import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.user.domain.OAuthProvider;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record NaverMemberDetail(
    String resultcode,
    String message,
    Response response
) {

  public OAuthMemberDetail toOAuthMemberDetail() {
    String profileImageUrl = response != null ? response.profile_image : null;

    return OAuthMemberDetail.of(
        getProfileImageOrDefault(profileImageUrl),
        response().email,
        OAuthProvider.NAVER,
        String.valueOf(response.id)
    );
  }

  private String getProfileImageOrDefault(String profileImage) {
    return profileImage == null ? DEFAULT_IMAGE_URL : profileImage;
  }

  @JsonNaming(SnakeCaseStrategy.class)
  private record Response(
      String id,
      String nickname,
      String email,
      String profile_image
  ) {

  }

}
