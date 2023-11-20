package coffeemeet.server.report.service.dto;

import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.domain.ReportReason;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public sealed interface ReportDto permits ReportDto.Response {

  record Response(
      String targetUserNickname,
      ReportReason reason,
      LocalDateTime createdAt
  ) implements ReportDto {

    public static Response of(User targetUser, Report report) {
      return new Response(
          targetUser.getProfile().getNickname(),
          report.getReason(),
          targetUser.getCreatedAt()
      );
    }
  }

}
