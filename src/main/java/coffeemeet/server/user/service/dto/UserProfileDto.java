package coffeemeet.server.user.service.dto;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.util.List;

public record UserProfileDto(
    String nickname,
    String profileImageUrl,
    String companyName,
    Department department,
    List<Keyword> interests
) {

  public static UserProfileDto of(User user, List<Keyword> interests, Certification certification) {
    return new UserProfileDto(
        user.getProfile().getNickname(),
        user.getOauthInfo().getProfileImageUrl(),
        certification.getCompanyName(),
        certification.getDepartment(),
        interests
    );
  }

}
