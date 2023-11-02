package coffeemeet.server.user.service.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.util.List;

public sealed interface UserProfileDto permits UserProfileDto.Response {

  record Response(
      String nickname,
      String profileImageUrl,
      Department department,
      List<Keyword> interests
  ) implements UserProfileDto {

    public static Response of(User user, Department department,
        List<Keyword> interests) {
      return new Response(
          user.getProfile().getNickname(),
          user.getProfile().getProfileImageUrl(),
          department,
          interests
      );
    }
  }

}
