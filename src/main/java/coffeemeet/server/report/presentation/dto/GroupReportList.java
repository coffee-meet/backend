package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.GroupReportDto;
import java.util.List;

public record GroupReportList(List<GroupReportDto> reports) {

  public static GroupReportList from(List<GroupReportDto> response) {
    return new GroupReportList(response);
  }

}

