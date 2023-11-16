package coffeemeet.server.report.service.dto;

import coffeemeet.server.report.domain.Report;
import java.time.LocalDateTime;

public sealed interface ReportDto permits ReportDto.Response {

  record Response(
      Long reporterId,
      Long chattingRoomId,
      Long targetId,
      String reporterEmail,
      String reason,
      String reasonDetail,
      LocalDateTime createAt
  ) implements ReportDto {

    public static Response of(Report report) {
      return new Response(
          report.getReporterId(),
          report.getChattingRoomId(),
          report.getTargetId(),
          report.getReporterEmail().getValue(),
          report.getReason(),
          report.getReasonDetail(),
          report.getCreatedAt()
      );
    }
  }

}
