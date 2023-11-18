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

  @DisplayName("신고 내역 중복 체크를 할 수 있다.")
  @Test
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
    Long reportId = report.getReporterId();

    given(reportRepository.findById(anyLong())).willReturn(Optional.of(report));

    // when
    Report foundReport = reportQuery.getReportById(reportId);

    // then
    assertThat(foundReport).isEqualTo(report);
  }

  @DisplayName("동일한 신고 대상 아이디와 채팅방 아이디를 가진 신고 내역을 조회할 수 있다.")
  @Test
  void getReportsByTargetIdAndChattingRoomIdTest() {
    // given
    Report report = report();
    Report report1 = report();
    Report sameReport = report(report.getTargetId(), report.getChattingRoomId());
    List<Report> reports = new ArrayList<>(List.of(report, report1, sameReport));
    List<Report> expectedReports = new ArrayList<>(List.of(report, sameReport));
    reportRepository.saveAll(reports);

    Long targetId = report.getTargetId();
    Long chattingRoomId = report.getChattingRoomId();

    given(reportRepository.findByTargetIdAndChattingRoomId(anyLong(), anyLong())).willReturn(
        expectedReports);
    // when
    List<Report> foundReports = reportQuery.getReportsByTargetIdAndChattingRoomId(
        targetId, chattingRoomId);

    // then
    assertThat(foundReports).isEqualTo(expectedReports);
  }

  @DisplayName("모든 신고 내역 조회 시 신고 대상과 채팅방이 동일한 경우 하나의 신고 내역으로 조회된다.")
  @Test
  void getAllReportsTest() {
    // given
    Report report = report();
    Report report1 = report();
    Report sameReport = report(report.getTargetId(), report.getChattingRoomId());
    List<Report> reports = new ArrayList<>(List.of(report, report1, sameReport));
    List<Report> expectedReports = new ArrayList<>(List.of(report1, sameReport));
    reportRepository.saveAll(reports);

    given(reportRepository.findAllReportsGroupedByTargetIdAndChattingRoomId()).willReturn(
        expectedReports);

    // when
    List<Report> allReports = reportQuery.getAllReports();

    // then
    assertThat(allReports).isEqualTo(expectedReports);
  }

}
