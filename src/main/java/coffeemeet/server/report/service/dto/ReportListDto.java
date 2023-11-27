package coffeemeet.server.report.service.dto;

import java.util.List;

public record ReportListDto(
    List<ReportDto> contents,
    boolean hasNext
) {

  public static ReportListDto of(List<ReportDto> contents, boolean hasNext) {
    return new ReportListDto(contents, hasNext);
  }

}
