package coffeemeet.server.report.service;

import static coffeemeet.server.common.fixture.entity.ReportFixture.report;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
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
import coffeemeet.server.report.service.dto.ReportDetailDto;
import coffeemeet.server.report.service.dto.ReportDetailDto.Response;
import coffeemeet.server.report.service.dto.TargetReportDto;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
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
        () -> reportService.reportUser(reporterId, report.getChattingRoomId(),
            report.getTargetedId(),
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
        () -> AssertionsForClassTypes.assertThat(result.targetedNickname())
            .isEqualTo(response.targetedNickname()),
        () -> AssertionsForClassTypes.assertThat(result.reason()).isEqualTo(response.reason()),
        () -> AssertionsForClassTypes.assertThat(result.reasonDetail())
            .isEqualTo(response.reasonDetail())
    );
  }

  // TODO: 11/21/23 test 미완성
//  @DisplayName("모든 신고 내역 조회 시, 한 채팅방에서 신고 대상이 동일하지 않다면 모든 대상이 조회된다.")
//  @Test
//  void findAllReportsTest() {
//    // given
//    Long chattingRoomId = 1L;
//
//    User targetUser1 = user();
//    User targetUser2 = user();
//
//    Report report1 = report(targetUser1.getId(), chattingRoomId);
//    Report report2 = report(targetUser2.getId(), chattingRoomId);
//    List<Report> reports = List.of(report1, report2);
//
//    Set<ChattingRoom> chattingRooms = ChattingFixture.chattingRoom(reports.size());
//    Set<User> users = user(reports.size());
//
//    given(reportQuery.getAllReports()).willReturn(reports);
//    given(userQuery.getUsersByIdSet(anySet())).willReturn(users);
//    given(chattingRoomQuery.getUserByIdSet(anySet())).willReturn(chattingRooms);
//    given(userQuery.getUserById(anyLong())).willReturn(targetUser1);
//
//    // when
//    List<ReportDto.Response> response = reportService.findAllReports();
//
//    // then
//    assertThat(response.size()).isEqualTo(reports.size());
//  }

  @Test
  @DisplayName("신고 대상 아이디와 채팅방 아이디로 해당하는 신고 내역을 조회할 수 있다.")
  void findReportByTargetIdAndChattingRoomIdTest() {
    // given
    User targetUser = user();
    Report report = report();
    Long targetId = report.getTargetedId();
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
    assertThat(response).hasSize(reports.size());
  }

}
