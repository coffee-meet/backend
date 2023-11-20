package coffeemeet.server.report.infrastructure;

import static coffeemeet.server.common.fixture.entity.ReportFixture.report;
import static org.junit.jupiter.api.Assertions.assertTrue;

import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.report.domain.Report;
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
    Long targetId = report.getTargetedId();
    Long chattingRoomId = report.getChattingRoomId();

    // when, then
    assertTrue(
        reportRepository.existsByReporterIdAndChattingRoomIdAndTargetedId(reporterId,
            chattingRoomId,
            targetId));
  }

}
