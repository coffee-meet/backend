package coffeemeet.server.user.service.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface MyProfileDto permits MyProfileDto.Response {

  record Response(
      String nickname,
      String email,
      String profileImageUrl,
      int reportedCount,
      LocalDateTime sanctionPeriod,
      Department department,
      List<Keyword> interests
  ) implements MyProfileDto {

    public static Response of(User user, List<Keyword> interests, Department department) {
      return new Response(
          user.getProfile().getNickname(),
          user.getProfile().getEmail().getEmail(),
          user.getProfile().getProfileImageUrl(),
          user.getReportInfo().getReportedCount(),
          user.getReportInfo().getSanctionPeriod(),
          department,
          interests
      );
    }
  }

}
