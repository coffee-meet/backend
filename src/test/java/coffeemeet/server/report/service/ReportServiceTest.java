package coffeemeet.server.report.service;

import static coffeemeet.server.common.fixture.entity.ReportFixture.report;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.report.implement.ReportQuery;
import coffeemeet.server.report.service.dto.ReportDto;
import coffeemeet.server.report.service.dto.ReportDetailDto;
import coffeemeet.server.report.service.dto.ReportDetailDto.Response;
import coffeemeet.server.report.service.dto.TargetReportDto;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  @InjectMocks
  private ReportService reportService;

  @Mock
  private ReportQuery reportQuery;

  @Mock
  private ReportCommand reportCommand;

  @Mock
  private UserQuery userQuery;

  @Mock
  private ChattingRoomQuery chattingRoomQuery;

  @DisplayName("신고할 수 있다.")
  @Test
  void reportUserTest() {
    // given
    Report report = report();
    Long reporterId = report.getReporterId();

    willDoNothing().given(reportQuery).hasDuplicatedReport(anyLong(), anyLong(), anyLong());
    willDoNothing().given(chattingRoomQuery).existsById(anyLong());
    willDoNothing().given(reportCommand).createReport(any(Report.class));

    // when
    assertThatCode(
        () -> reportService.reportUser(reporterId, report.getChattingRoomId(), report.getTargetId(),
            "부적절한 콘텐츠", "신고 상세 내용")).doesNotThrowAnyException();
  }

  @DisplayName("신고 아이디로 해당 신고 내역을 조회할 수 있다.")
  @Test
  void findReportTest() {
    // given
    Report report = report();
    User reporter = user();
    User targetUser = user();
    ReportDetailDto.Response response = Response.of(report, reporter, targetUser);

    given(reportQuery.getReportById(anyLong())).willReturn(report);
    given(userQuery.getUserById(anyLong())).willReturn(reporter);
    given(userQuery.getUserById(anyLong())).willReturn(targetUser);

    // when
    ReportDetailDto.Response result = reportService.findReportById(report.getId());

    // then
    assertAll(
        () -> assertThat(result.targetNickname()).isEqualTo(response.targetNickname()),
        () -> assertThat(result.reason()).isEqualTo(response.reason()),
        () -> assertThat(result.reasonDetail()).isEqualTo(response.reasonDetail())
    );
  }

  @DisplayName("모든 신고 내역 조회 시 신고 대상과 채팅방이 동일한 경우 하나의 신고 내역으로 조회된다.")
  @Test
  void findAllReportsTest() {
    // given
    User targetUser = user();
    Report report = report();
    Report report1 = report();
    List<Report> reports = List.of(report, report1);

    given(reportQuery.getAllReports()).willReturn(reports);
    given(userQuery.getUserById(anyLong())).willReturn(targetUser);

    // when
    List<ReportDto.Response> response = reportService.findAllReports();

    // then
    assertThat(response.size()).isEqualTo(reports.size());
  }

  @Test
  @DisplayName("신고 대상 아이디와 채팅방 아이디로 해당하는 신고 내역을 조회할 수 있다.")
  void findReportByTargetIdAndChattingRoomIdTest() {
    // given
    User targetUser = user();
    Report report = report();
    Long targetId = report.getTargetId();
    Long chattingRoomId = report.getChattingRoomId();
    Report sameReport = report(targetId, chattingRoomId);
    List<Report> reports = List.of(report, sameReport);

    given(reportQuery.getReportsByTargetIdAndChattingRoomId(anyLong(), anyLong())).willReturn(
        reports);
    given(userQuery.getUserById(anyLong())).willReturn(targetUser);

    // when
    List<TargetReportDto.Response> response = reportService.findReportByTargetIdAndChattingRoomId(
        targetId, chattingRoomId);

    // given
    assertThat(response.size()).isEqualTo(reports.size());
  }

}
