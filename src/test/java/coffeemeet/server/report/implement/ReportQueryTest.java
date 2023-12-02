package coffeemeet.server.report.implement;

import static coffeemeet.server.common.fixture.entity.ReportFixture.report;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.execption.DuplicatedDataException;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.infrastructure.ReportQueryRepository;
import coffeemeet.server.report.infrastructure.ReportRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

  @Mock
  private ReportQueryRepository reportQueryRepository;

  @DisplayName("중복된 신고가 아닐 경우 패스한다.")
  @Test
  void hasDuplicatedReportTest() {
    // given
    long reporterId = 1L;
    long chattingRoomId = 1L;
    long targetId = 1L;

    given(reportRepository.existsByReporterIdAndChattingRoomIdAndTargetedId(anyLong(), anyLong(),
        anyLong())).willReturn(Boolean.FALSE);

    // when, then
    assertThatCode(() -> reportQuery.hasDuplicatedReport(reporterId, chattingRoomId, targetId))
        .doesNotThrowAnyException();
  }

  @DisplayName("중복된 신고일 경우 예외를 던진다.")
  @Test
  void hasNotDuplicatedReportTest() {
    // given
    long reporterId = 1L;
    long chattingRoomId = 1L;
    long targetId = 1L;

    given(reportRepository.existsByReporterIdAndChattingRoomIdAndTargetedId(anyLong(), anyLong(),
        anyLong())).willReturn(Boolean.TRUE);

    // when, then
    assertThatThrownBy(() -> reportQuery.hasDuplicatedReport(reporterId, chattingRoomId, targetId))
        .isInstanceOf(DuplicatedDataException.class);
  }

  @DisplayName("신고 아이디로 신고를 조회할 수 있다.")
  @Test
  void getReportByIdTest() {
    // given
    Report report = report();

    given(reportQueryRepository.findById(anyLong())).willReturn(Optional.of(report));

    // when
    Report foundReport = reportQuery.getReportById(report.getReporterId());

    // then
    assertAll(
        () -> assertThat(foundReport.getReporterId())
            .isEqualTo(foundReport.getReporterId()),
        () -> assertThat(foundReport.getReason())
            .isEqualTo(foundReport.getReason()),
        () -> assertThat(foundReport.getReasonDetail())
            .isEqualTo(foundReport.getReasonDetail()),
        () -> assertThat(foundReport.getChattingRoomId()).isEqualTo(
            foundReport.getChattingRoomId()),
        () -> assertThat(foundReport.getTargetedId())
            .isEqualTo(foundReport.getTargetedId()),
        () -> assertThat(foundReport.isProcessed())
            .isEqualTo(foundReport.isProcessed())
    );
  }

  @DisplayName("신고 아이디로 해당 신고들을 모두 조회할 수 있다.")
  @Test
  void getReportsByIdSetTest() {
    // given
    Report report = report();
    Report report1 = report();
    List<Report> reports = List.of(report, report1);
    Set<Long> reportIds = Set.of(report.getReporterId(), report1.getReporterId());

    given(reportRepository.findByIdIn(reportIds)).willReturn(reports);

    // when
    List<Report> result = reportQuery.getReportsByIdSet(reportIds);

    // then
    assertThat(result).contains(report, report1);
  }

  @DisplayName("신고 대상 아이디와 채팅방 아이디로 해당 신고 내역을 조회할 수 있다.")
  @Test
  void getReportsByTargetIdAndChattingRoomIdTest() {
    // given
    Report report = report();
    Report report1 = report(report.getTargetedId(), report.getChattingRoomId());
    List<Report> reports = List.of(report, report1);

    given(reportQueryRepository.findByTargetIdAndChattingRoomId(anyLong(), anyLong())).willReturn(
        reports);

    // when
    List<Report> result = reportQuery.getReportsByTargetIdAndChattingRoomId(
        report.getTargetedId(), report1.getChattingRoomId());

    // then
    assertThat(result).contains(report, report1);
  }

  @DisplayName("신고 대상 아이디와 채팅방 아이디로 해당 신고 내역을 조회할 경우, 신고 내역이 없다면 예외를 발생한다.")
  @Test
  void getReportsByTargetIdAndChattingRoomIdFailTest() {
    // given
    long targetedId = 1;
    long chattingRoomId = 1;
    List<Report> reports = new ArrayList<>();

    given(reportQueryRepository.findByTargetIdAndChattingRoomId(anyLong(), anyLong())).willReturn(
        reports);

    // when, then
    assertThatThrownBy(
        () -> reportQuery.getReportsByTargetIdAndChattingRoomId(targetedId, chattingRoomId))
        .isInstanceOf(NotFoundException.class);
  }

  @DisplayName("모든 신고 내역을 조회할 수 있다.")
  @Test
  void getAllReportsTest() {
    // given
    Long lastReportId = 10L;
    int pageSize = 10;
    Report report = report();
    Report report1 = report();
    List<Report> reports = List.of(report, report1);

    given(reportQueryRepository.findAll(anyLong(), anyInt())).willReturn(reports);

    // when
    List<Report> result = reportQuery.getAllReports(lastReportId, pageSize);

    // then
    assertThat(result).contains(report, report1);
  }

}
