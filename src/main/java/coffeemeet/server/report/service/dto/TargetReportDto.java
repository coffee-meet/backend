package coffeemeet.server.report.service.dto;

import java.time.LocalDateTime;

public sealed interface TargetReportDto permits TargetReportDto.Response {

  record Response(
      String reporterNickname,
      LocalDateTime createdAt
  ) implements TargetReportDto {

    public static Response of(String reporterNickname, LocalDateTime createdAt) {
      return new Response(
          reporterNickname,
          createdAt
      );
    }
  }

}
