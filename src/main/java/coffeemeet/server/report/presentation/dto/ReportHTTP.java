package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.ReportDto;
import java.time.LocalDateTime;

public final class ReportHTTP {

  public record Response(
      String targetedNickname,
      String chattingRoomName,
      LocalDateTime createdAt
  ) {

    public static Response from(ReportDto.Response response) {
      return new Response(
          response.targetedNickname(),
          response.chattingRoomName(),
          response.createdAt()
      );
    }
  }

}
