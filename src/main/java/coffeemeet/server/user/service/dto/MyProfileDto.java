package coffeemeet.server.user.service.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class MyProfileDto {

  public record Response(
      String nickname,
      String email,
      String profileImageUrl,
      int reportedCount,
      LocalDateTime sanctionPeriod,
      Department department,
      List<Keyword> interests
  ) {

    public static Response of(User user, List<Keyword> interests, Department department) {
      return new Response(
          user.getProfile().getNickname(),
          user.getProfile().getEmail().getValue(),
          user.getProfile().getProfileImageUrl(),
          user.getReportInfo().getReportedCount(),
          user.getReportInfo().getPenaltyExpiration(),
          department,
          interests
      );
    }
  }

}
