package coffeemeet.server.report.service.dto;

import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.domain.ReportReason;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public sealed interface ReportDto permits ReportDto.Response {

  record Response(
      String reporterNickname,
      String targetNickname,
      String targetEmail,
      ReportReason reason,
      String reasonDetail,
      int reportedCount,
      LocalDateTime createAt
  ) implements ReportDto {

    public static Response of(Report report, User reporter, User targetUser) {
      return new Response(
          reporter.getProfile().getNickname(),
          targetUser.getProfile().getNickname(),
          targetUser.getProfile().getEmail().getValue(),
          report.getReason(),
          report.getReasonDetail(),
          targetUser.getReportInfo().getReportedCount(),
          report.getCreatedAt()
      );
    }
  }

}
