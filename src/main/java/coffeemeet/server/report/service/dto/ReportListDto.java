package coffeemeet.server.report.service.dto;

import java.util.List;

public record ReportListDto(
    List<Report> contents,
    boolean hasNext
) {

  public static ReportListDto of(List<Report> contents, boolean hasNext) {
    return new ReportListDto(contents, hasNext);
  }

}
