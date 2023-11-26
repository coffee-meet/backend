package coffeemeet.server.user.service.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.util.List;

public record UserProfileDto(
    String nickname,
    String profileImageUrl,
    Department department,
    List<Keyword> interests
) {

  public static UserProfileDto of(
      User user,
      Department department,
      List<Keyword> interests
  ) {
    return new UserProfileDto(
        user.getProfile().getNickname(),
        user.getOauthInfo().getProfileImageUrl(),
        department,
        interests
    );
  }

}
