package coffeemeet.server.user.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface MyProfileDto permits MyProfileDto.Response {

  record Response(
      String name,
      String nickname,
      String email,
      String profileImageUrl,
      String birthYear,
      String birthDay,
      int reportedCount,
      LocalDateTime sanctionPeriod,
      Department department,
      List<Keyword> interests
  ) implements MyProfileDto {

    public static Response of(User user, List<Keyword> interests, Department department) {

      return new Response(
          user.getProfile().getName(),
          user.getProfile().getNickname(),
          user.getProfile().getEmail().getEmail(),
          user.getProfile().getProfileImageUrl(),
          user.getProfile().getBirth().getBirthYear(),
          user.getProfile().getBirth().getBirthDay(),
          user.getReportInfo().getReportedCount(),
          user.getReportInfo().getSanctionPeriod(),
          department,
          interests
      );
    }
  }

}
