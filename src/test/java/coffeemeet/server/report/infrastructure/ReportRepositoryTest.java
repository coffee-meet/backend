package coffeemeet.server.report.infrastructure;

import static coffeemeet.server.common.fixture.entity.ReportFixture.report;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.report.domain.Report;
import java.util.List;
import java.util.Objects;
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

  @Test
  @DisplayName("신고자 아이디, 채팅방 아이디, 신고 대상 아이디로 신고 내역 존재 여부를 알 수 있다.")
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

  @Test
  @DisplayName("신고 대상 아이디와 채팅방 아이디와 일치하는 신고 내역을 조회할 수 있다.")
  void findByTargetIdAndChattingRoomIdTest() {
    // given
    Report report = report();
    Report report1 = report(report.getTargetId(), report.getChattingRoomId());
    reportRepository.save(report);
    reportRepository.save(report1);

    Long targetId = report.getTargetId();
    Long chattingRoomId = report.getChattingRoomId();

    // when
    List<Report> reports = reportRepository.findByTargetIdAndChattingRoomId(
        targetId, chattingRoomId);
    long sameTargetId = reports.stream()
        .filter(rep -> Objects.equals(rep.getTargetId(), report.getTargetId()))
        .count();

    // then
    assertAll(
        () -> assertThat(reports.size()).isEqualTo(2),
        () -> assertThat(sameTargetId).isEqualTo(2)
    );
  }

  @Test
  @DisplayName("모든 신고 내역 조회 시 신고 대상과 채팅방이 동일한 경우 하나의 신고 내역으로 조회된다.")
  void findAllReportsGroupedByTargetIdAndChattingRoomIdTest() {
    // given
    Report report = report();
    Report report1 = report(report.getTargetId(), report.getChattingRoomId());
    Report report2 = report();
    reportRepository.save(report);
    reportRepository.save(report1);
    reportRepository.save(report2);

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
