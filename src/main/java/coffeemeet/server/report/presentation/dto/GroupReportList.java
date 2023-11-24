package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.GroupReportDto;
import java.util.List;

public record GroupReportList(List<GroupReportDto.Response> reports) {

  public static GroupReportList from(List<GroupReportDto.Response> response) {
    return new GroupReportList(response);
  }

}

