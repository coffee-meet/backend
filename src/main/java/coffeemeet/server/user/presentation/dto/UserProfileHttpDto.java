package coffeemeet.server.user.presentation.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.service.dto.UserProfileDto;
import java.util.List;

public sealed interface UserProfileHttpDto permits UserProfileHttpDto.Response {

  record Response(
      String nickname,
      String profileImageUrl,
      Department department,
      List<Keyword> interests
  ) implements UserProfileHttpDto {

    public static Response of(UserProfileDto.Response response) {
      return new Response(
          response.nickname(),
          response.profileImageUrl(),
          response.department(),
          response.interests()
      );
    }
  }

}