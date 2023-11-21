package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.report.presentation.dto.TargetReportHTTP;
import coffeemeet.server.report.service.dto.TargetReportDto;

public class TargetReportHTTPFixture {

  public static TargetReportHTTP.Response targatReportHTTPResponse(
      TargetReportDto.Response response) {
    return new TargetReportHTTP.Response(
        response.reporterNickname(),
        response.createdAt()
    );
  }

}
