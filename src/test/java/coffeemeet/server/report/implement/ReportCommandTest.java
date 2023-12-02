package coffeemeet.server.report.implement;

import static coffeemeet.server.common.fixture.entity.ReportFixture.report;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.infrastructure.ReportRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportCommandTest {

  @InjectMocks
  private ReportCommand reportCommand;

  @Mock
  private ReportQuery reportQuery;

  @Mock
  private ReportRepository reportRepository;

  @DisplayName("신고를 생성해 저장할 수 있다.")
  @Test
  void createReportTest() {
    // given
    Report report = report();

    given(reportRepository.save(any(Report.class))).willReturn(report);

    // when, then
    assertThatCode(() -> reportCommand.createReport(report)).doesNotThrowAnyException();
  }

  @DisplayName("신고를 처리할 수 있다.")
  @Test
  void processReportsTest() {
    // given
    Report report = report();
    Report report1 = report();
    List<Report> reports = List.of(report, report1);
    Set<Long> reportIds = Set.of(report.getReporterId(), report1.getReporterId());

    given(reportQuery.getReportsByIdSet(anySet())).willReturn(reports);

    // when, then
    assertThatCode(() -> reportCommand.processReports(reportIds))
        .doesNotThrowAnyException();
  }

  @DisplayName("신고를 삭제할 수 있다.")
  @Test
  void deleteReportsTest() {
    // given
    Report report = report();
    Report report1 = report();
    Set<Long> reportIds = Set.of(report.getReporterId(), report1.getReporterId());

    // when, then
    assertThatCode(() -> reportCommand.deleteReports(reportIds))
        .doesNotThrowAnyException();
  }

}
