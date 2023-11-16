package coffeemeet.server.report.implement;

import coffeemeet.server.report.domain.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ReportCommand {

  private final ReportQuery reportQuery;

  public void processReport(Long reportId) {
    Report report = reportQuery.getReportById(reportId);
    report.processed();
  }

}
