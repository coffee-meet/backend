package coffeemeet.server.report.service.dto;

import java.time.LocalDateTime;

public record GroupReportDto(
    String reporterNickname,
    Long reportId,
    LocalDateTime createdAt
) {

  public static GroupReportDto of(String reporterNickname, Long reportId, LocalDateTime createdAt) {
    return new GroupReportDto(
        reporterNickname,
        reportId,
        createdAt
    );
  }

}
