package coffeemeet.server.report.implement;

import static coffeemeet.server.common.fixture.entity.ReportFixture.report;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.infrastructure.ReportRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportQueryTest {

  @InjectMocks
  private ReportQuery reportQuery;

  @Mock
  private ReportRepository reportRepository;

  @Test
  @DisplayName("신고 내역 중복 체크를 할 수 있다.")
  void hasDuplicatedReportTest() {
    // given
    long reporterId = 1L;
    long chattingRoomId = 1L;
    long targetId = 1L;

    given(reportRepository.existsByReporterIdAndChattingRoomIdAndTargetId(anyLong(), anyLong(),
        anyLong())).willReturn(Boolean.FALSE);

    // when, then
    assertThatCode(() -> reportQuery.hasDuplicatedReport(reporterId, chattingRoomId, targetId))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("신고 아이디를 통해 신고를 조회할 수 있다.")
  void getReportByIdTest() {
    // given
    Report report = report();
    reportRepository.save(report);
    Long reporterId = report.getReporterId();

    given(reportRepository.findById(anyLong())).willReturn(Optional.of(report));

    // when, then
    assertThat(reportQuery.getReportById(reporterId)).isEqualTo(report);
  }

  @Test
  @DisplayName("모든 신고 내역을 조회할 수 있다.")
  void getAllReportsTest() {
    // given
    List<Report> reports = new ArrayList<>(List.of(report(), report()));

    given(reportRepository.findAll()).willReturn(reports);

    // when, then
    assertThat(reportQuery.getAllReports()).isEqualTo(reports);
  }

}
