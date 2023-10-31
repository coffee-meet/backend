package coffeemeet.server.user.controller.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.service.dto.MyProfileDto;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface MyProfileHttpDto permits MyProfileHttpDto.Response {

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
  ) implements MyProfileHttpDto {

    public static Response of(MyProfileDto.Response response) {
      return new Response(
          response.name(),
          response.nickname(),
          response.email(),
          response.profileImageUrl(),
          response.birthYear(),
          response.birthDay(),
          response.reportedCount(),
          response.sanctionPeriod(),
          response.department(),
          response.interests()
      );
    }
  }

}
