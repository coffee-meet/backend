package coffeemeet.server.report.infrastructure;

import static coffeemeet.server.common.fixture.entity.ReportFixture.report;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.report.domain.Report;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReportRepositoryTest extends RepositoryTestConfig {

  @Autowired
  private ReportRepository reportRepository;

  @AfterEach
  void tearDown() {
    reportRepository.deleteAll();
  }

  @DisplayName("신고자 아이디, 채팅방 아이디, 신고 대상 아이디로 신고 내역 존재 여부를 알 수 있다.")
  @Test
  void existsByReporterIdAndChattingRoomIdAndTargetIdTest() {
    // given
    Report report = report();
    reportRepository.save(report);

    Long reporterId = report.getReporterId();
    Long targetId = report.getTargetId();
    Long chattingRoomId = report.getChattingRoomId();

    // when, then
    assertTrue(
        reportRepository.existsByReporterIdAndChattingRoomIdAndTargetId(reporterId, chattingRoomId,
            targetId));
  }

  @DisplayName("신고 대상 아이디와 채팅방 아이디와 일치하는 신고 내역을 조회할 수 있다.")
  @Test
  void findByTargetIdAndChattingRoomIdTest() {
    // given
    Report report = report();
    Report sameReport = report(report.getTargetId(), report.getChattingRoomId());
    reportRepository.save(report);
    reportRepository.save(sameReport);

    List<Report> expectedReports = new ArrayList<>(List.of(report, sameReport));
    Long targetId = report.getTargetId();
    Long chattingRoomId = report.getChattingRoomId();

    // when
    List<Report> reports = reportRepository.findByTargetIdAndChattingRoomId(
        targetId, chattingRoomId);

    // then
    assertAll(
        () -> assertThat(reports.size()).isEqualTo(expectedReports.size()),
        () -> assertThat(reports.get(0).getTargetId()).isEqualTo(
            expectedReports.get(1).getTargetId()),
        () -> assertThat(reports.get(0).getChattingRoomId()).isEqualTo(
            expectedReports.get(1).getChattingRoomId())
    );
  }

  @DisplayName("모든 신고 내역 조회 시 신고 대상과 채팅방이 동일한 경우 하나의 신고 내역으로 조회된다.")
  @Test
  void findAllReportsGroupedByTargetIdAndChattingRoomIdTest() {
    // given
    Report report = report();
    Report report1 = report();
    Report sameReport = report(report.getTargetId(), report.getChattingRoomId());
    reportRepository.save(report);
    reportRepository.save(report1);
    reportRepository.save(sameReport);

    // when
    List<Report> allReports = reportRepository.findAllReportsGroupedByTargetIdAndChattingRoomId();

    // then
    assertAll(
        () -> assertThat(allReports.size()).isEqualTo(2),
        () -> assertThat(allReports.get(0).getChattingRoomId()).isNotEqualTo(
            allReports.get(1).getChattingRoomId()),
        () -> assertThat(allReports.get(0).getTargetId()).isNotEqualTo(
            allReports.get(1).getTargetId())
    );
  }

}
