package coffeemeet.server.report.infrastructure;

import static coffeemeet.server.common.fixture.entity.ReportFixture.report;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.report.domain.Report;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReportQueryRepositoryTest extends RepositoryTestConfig {

  @Autowired
  private ReportQueryRepository reportQueryRepository;

  @Autowired
  private ReportRepository reportRepository;

  @BeforeEach
  void setup() {
    reportRepository.deleteAll();
  }

  @DisplayName("모든 신고 내역 조회 시 신고 대상 아이디와 채팅방 아이디가 같다면 가장 최신 내역만 보여준다.")
  @Test
  void findAllTest() {
    //given
    Report report = report();
    Report report1 = report();
    Report report2 = report();
    Report sameReport1 = report(report1.getTargetId(), report1.getChattingRoomId());
    Report sameReport2 = report(report2.getTargetId(), report2.getChattingRoomId());

    List<Report> reports = List.of(report, report1, report2, sameReport1, sameReport2);
    List<Report> expectedReports = List.of(report, report1, report2);
    reportRepository.saveAll(reports);

    // when
    List<Report> allReports = reportQueryRepository.findAll();

    // then
    assertThat(allReports.size()).isEqualTo(expectedReports.size());
  }

  @DisplayName("신고 대상 아이디와 채팅방 아이디와 일치하는 신고 내역을 조회할 수 있다.")
  @Test
  void findByTargetIdAndChattingRoomIdTest() {
    // given
    Report report = report();
    Report sameReport = report(report.getTargetId(), report.getChattingRoomId());
    reportRepository.save(report);
    reportRepository.save(sameReport);

    List<Report> expectedReports = List.of(report, sameReport);
    Long targetId = report.getTargetId();
    Long chattingRoomId = report.getChattingRoomId();

    // when
    List<Report> reports = reportQueryRepository.findByTargetIdAndChattingRoomId(
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
    List<Report> reports = List.of(report, report1, sameReport);
    reportRepository.saveAll(reports);

    // when
    List<Report> allReports = reportQueryRepository.findAll();

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
