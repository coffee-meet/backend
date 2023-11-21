package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.TargetReportDto;
import java.time.LocalDateTime;

public final class TargetReportHTTP {

  public record Response(
      String reporterNickname,
      LocalDateTime createdAt
  ) {

    public static Response from(TargetReportDto.Response response) {
      return new Response(
          response.reporterNickname(),
          response.createdAt()
      );
    }
  }

}
