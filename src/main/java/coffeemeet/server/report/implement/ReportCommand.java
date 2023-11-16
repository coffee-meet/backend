package coffeemeet.server.report.implement;

import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.infrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportCommand {

  private final ReportRepository reportRepository;

  public void save(Report report) {
    reportRepository.save(report);
  }

}
