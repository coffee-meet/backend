package coffeemeet.server.user.presentation.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.service.dto.MyProfileDto;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface MyProfileHTTP permits MyProfileHTTP.Response {

  record Response(
      String nickname,
      String email,
      String profileImageUrl,
      int reportedCount,
      LocalDateTime sanctionPeriod,
      Department department,
      List<Keyword> interests
  ) implements MyProfileHTTP {

    public static Response of(MyProfileDto.Response response) {
      return new Response(
          response.nickname(),
          response.email(),
          response.profileImageUrl(),
          response.reportedCount(),
          response.sanctionPeriod(),
          response.department(),
          response.interests()
      );
    }
  }

}
