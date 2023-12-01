package coffeemeet.server.user.service.dto;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.User;
import java.util.List;

public record MyProfileDto(
    String nickname,
    String profileImageUrl,
    String companyName,
    Department department,
    List<Keyword> interests,
    OAuthProvider oAuthProvider

) {

  public static MyProfileDto of(
      User user,
      List<Keyword> interests,
      Certification certification
  ) {
    return new MyProfileDto(
        user.getProfile().getNickname(),
        user.getOauthInfo().getProfileImageUrl(),
        certification.getCompanyName(),
        certification.getDepartment(),
        interests,
        user.getOauthInfo().getOauthProvider()
    );
  }

}
