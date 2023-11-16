package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.AllReportDto;
import java.time.LocalDateTime;

public sealed interface AllReportHTTP permits AllReportHTTP.Response {

  record Response(
      String targetNickname,
      int targetReportedCount,
      LocalDateTime createdAt
  ) implements AllReportHTTP {

    public static Response of(AllReportDto.Response response) {
      return new Response(
          response.targetUserNickname(),
          response.reportedCount(),
          response.createdAt()
      );
    }
  }

}
