package coffeemeet.server.report.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.report.service.dto.GroupReportDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class GroupReportHTTP {

  public record GroupReport(
      String reporterNickname,
      Long reportId,
      LocalDateTime createdAt
  ) {

    public static GroupReport from(GroupReportDto response) {
      return new GroupReport(
          response.reporterNickname(),
          response.reportId(),
          response.createdAt()
      );
    }
  }

  public record Response(List<GroupReport> groupReports) {

    public static Response from(List<GroupReportDto> responses) {
      List<GroupReport> groupReportList = responses.stream()
          .map(GroupReport::from)
          .toList();
      return new Response(groupReportList);
    }
  }

}
