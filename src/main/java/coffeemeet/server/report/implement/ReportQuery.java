package coffeemeet.server.report.implement;

import static coffeemeet.server.report.exception.ReportErrorCode.REPORT_NOT_FOUND;

import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.infrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportQuery {

  private static final String REPORT_NOT_FOUND_ERROR_MESSAGE = "해당 id(%s)에 매칭되는 신고가 없습니다.";
  private final ReportRepository reportRepository;

  public Report getReportById(Long reportId) {
    return reportRepository.findById(reportId)
        .orElseThrow(() -> new NotFoundException(REPORT_NOT_FOUND,
            String.format(REPORT_NOT_FOUND_ERROR_MESSAGE, reportId)));
  }

}
