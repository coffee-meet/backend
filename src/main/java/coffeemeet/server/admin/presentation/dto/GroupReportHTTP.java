package coffeemeet.server.admin.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.report.service.dto.GroupReportDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class GroupReportHTTP {

  public record Response(List<GroupReportDto> groupReports) {

    public static Response from(List<GroupReportDto> responses) {
      return new Response(responses);
    }
  }

}
