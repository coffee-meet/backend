package coffeemeet.server.report.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record GroupReportDto(
    String reporterNickname,
    Long reportId,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
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
