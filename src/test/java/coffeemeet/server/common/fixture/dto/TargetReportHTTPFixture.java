package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.report.presentation.dto.GroupReportHTTP;
import coffeemeet.server.report.service.dto.GroupReportDto;

public class TargetReportHTTPFixture {

  public static GroupReportHTTP.Response targatReportHTTPResponse(
      GroupReportDto.Response response) {
    return new GroupReportHTTP.Response(
        response.reporterNickname(),
        response.createdAt()
    );
  }

}
