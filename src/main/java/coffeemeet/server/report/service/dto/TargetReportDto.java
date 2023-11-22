package coffeemeet.server.report.service.dto;

import java.time.LocalDateTime;

public final class TargetReportDto {

  public record Response(
      String reporterNickname,
      LocalDateTime createdAt
  ) {

    public static Response of(String reporterNickname, LocalDateTime createdAt) {
      return new Response(
          reporterNickname,
          createdAt
      );
    }
  }

}
