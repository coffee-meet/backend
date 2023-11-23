package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.GroupReportDto;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
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
