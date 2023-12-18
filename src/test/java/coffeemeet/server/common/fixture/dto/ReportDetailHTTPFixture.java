package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.admin.presentation.dto.ReportDetailHTTP;
import coffeemeet.server.report.service.dto.ReportDetailDto;

public class ReportDetailHTTPFixture {

  public static ReportDetailHTTP.Response reportDetailHTTPResponse(
      ReportDetailDto response) {
    return new ReportDetailHTTP.Response(
        response.reporterNickname(),
        response.targetedNickname(),
        response.targetedEmail(),
        response.reason(),
        response.reasonDetail(),
        response.reportedCount(),
        response.createdAt()
    );
  }

}
