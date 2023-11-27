package coffeemeet.server.report.service.dto;

import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.domain.ReportReason;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public record ReportDetailDto(
    String reporterNickname,
    String targetedNickname,
    String targetedEmail,
    ReportReason reason,
    String reasonDetail,
    int reportedCount,
    LocalDateTime createdAt
) {

  public static ReportDetailDto of(Report report, User reporter, User targeted) {
    return new ReportDetailDto(
        reporter.getProfile().getNickname(),
        targeted.getProfile().getNickname(),
        targeted.getOauthInfo().getEmail().getValue(),
        report.getReason(),
        report.getReasonDetail(),
        targeted.getReportInfo().getReportedCount(),
        report.getCreatedAt()
    );
  }

}
