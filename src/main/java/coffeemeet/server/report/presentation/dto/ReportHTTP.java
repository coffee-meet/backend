package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.ReportDto;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
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
