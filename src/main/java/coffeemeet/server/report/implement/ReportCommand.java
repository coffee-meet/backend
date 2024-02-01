package coffeemeet.server.report.implement;

import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.infrastructure.ReportRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportCommand {

  private final ReportQuery reportQuery;
  private final ReportRepository reportRepository;

  public void createReport(Report report) {
    reportRepository.save(report);
  }

  public void processReports(Set<Long> reportIds) {
    List<Report> reportsByIdSet = reportQuery.getReportsByIdSet(reportIds);
    reportsByIdSet.forEach(Report::processed);
  }

  public void deleteReports(Set<Long> reportIds) {
    reportRepository.deleteAllByIdInBatch(reportIds);
  }

}
