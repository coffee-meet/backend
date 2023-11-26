package coffeemeet.server.report.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.report.service.dto.GroupReportDto;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

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
