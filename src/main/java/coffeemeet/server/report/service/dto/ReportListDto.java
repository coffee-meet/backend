package coffeemeet.server.report.service.dto;

import java.util.List;

public record ReportListDto(
    List<ReportSummary> contents,
    boolean hasNext
) {

  public static ReportListDto of(List<ReportSummary> contents, boolean hasNext) {
    return new ReportListDto(contents, hasNext);
  }

}
