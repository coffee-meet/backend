package coffeemeet.server.common.fixture;

import static org.instancio.Select.field;

import coffeemeet.server.admin.presentation.dto.ReportDetailHTTP;
import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.service.dto.GroupReportDto;
import coffeemeet.server.report.service.dto.ReportDetailDto;
import coffeemeet.server.report.service.dto.ReportListDto;
import org.instancio.Instancio;

public class ReportFixture {

  public static Report report() {
    return Instancio.of(Report.class)
        .generate(field(Report::getReasonDetail), gen -> gen.string().maxLength(200)).create();
  }

  public static Report report(Long targetId, Long chattingRoomId) {
    return Instancio.of(Report.class)
        .set(field(Report::getTargetedId), targetId)
        .set(field(Report::getChattingRoomId), chattingRoomId)
        .generate(field(Report::getReasonDetail), gen -> gen.string().maxLength(200)).create();
  }

  public static GroupReportDto targetReportDto() {
    return Instancio.of(GroupReportDto.class)
        .create();
  }

  public static ReportDetailDto reportDetailDto() {
    return Instancio.of(ReportDetailDto.class)
        .create();
  }

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

  public static ReportListDto reportListDto() {
    return Instancio.of(ReportListDto.class)
        .create();
  }

}
