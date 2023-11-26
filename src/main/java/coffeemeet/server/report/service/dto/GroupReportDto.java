package coffeemeet.server.report.service.dto;

import java.time.LocalDateTime;

public record GroupReportDto(
    String reporterNickname,
    LocalDateTime createdAt
) {

  public static GroupReportDto of(String reporterNickname, LocalDateTime createdAt) {
    return new GroupReportDto(
        reporterNickname,
        createdAt
    );
  }

}
