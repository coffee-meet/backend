package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.GroupReportDto;
import java.time.LocalDateTime;

public final class GroupReportHTTP {

  public record Response(
      String reporterNickname,
      LocalDateTime createdAt
  ) {

    public static Response from(GroupReportDto.Response response) {
      return new Response(
          response.reporterNickname(),
          response.createdAt()
      );
    }
  }

}
