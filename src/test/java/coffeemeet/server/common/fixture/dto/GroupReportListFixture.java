package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.report.presentation.dto.GroupReportList;
import coffeemeet.server.report.service.dto.GroupReportDto;
import java.util.List;

public class GroupReportListFixture {

  public static GroupReportList groupReportListResponse(List<GroupReportDto> response) {
    return new GroupReportList(response);
  }

}
