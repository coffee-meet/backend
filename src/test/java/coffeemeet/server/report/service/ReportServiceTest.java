package coffeemeet.server.report.service;

import static coffeemeet.server.common.fixture.ReportFixture.report;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.history.implement.ChattingRoomHistoryQuery;
import coffeemeet.server.chatting.history.implement.UserChattingHistoryQuery;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.common.fixture.ChattingFixture;
import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.report.implement.ReportQuery;
import coffeemeet.server.report.service.dto.GroupReportDto;
import coffeemeet.server.report.service.dto.ReportDetailDto;
import coffeemeet.server.report.service.dto.ReportListDto;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import java.util.Set;
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

  @Mock
  private UserChattingHistoryQuery userChattingHistoryQuery;

  @Mock
  private ChattingRoomHistoryQuery chattingRoomHistoryQuery;

  @DisplayName("신고할 수 있다.")
  @Test
  void reportUserTest() {
    // given
    Report report = report();
    Long reporterId = report.getReporterId();

    willDoNothing().given(reportQuery).hasDuplicatedReport(anyLong(), anyLong(), anyLong());
    willDoNothing().given(chattingRoomQuery).verifyChatRoomExistence(anyLong());
    willDoNothing().given(reportCommand).createReport(any(Report.class));

    // when, then
    assertThatCode(
        () -> reportService.reportUser(reporterId, report.getChattingRoomId(),
            report.getTargetedId(),
            "부적절한 콘텐츠", "신고 상세 내용")).doesNotThrowAnyException();
  }

  @DisplayName("존재하지 않는 채팅방에 대해 신고할 경우 예외가 발생한다.")
  @Test
  void reportUserFailTest() {
    // given
    Report report = report();
    Long reporterId = report.getReporterId();

    willDoNothing().given(reportQuery).hasDuplicatedReport(anyLong(), anyLong(), anyLong());
    willThrow(NotFoundException.class).given(chattingRoomQuery).verifyChatRoomExistence(anyLong());
    given(userChattingHistoryQuery.existsByUserId(anyLong())).willReturn(Boolean.FALSE);

    // when, then
    assertThatThrownBy(() -> reportService.reportUser(reporterId, report.getChattingRoomId(),
        report.getTargetedId(), "잠수", "잠수타요."))
        .isInstanceOf(NotFoundException.class);
  }

  @DisplayName("신고 아이디로 해당 신고 내역을 조회할 수 있다.")
  @Test
  void findReportTest() {
    // given
    Report report = report();
    User reporter = user();
    User targetUser = user();
    ReportDetailDto response = ReportDetailDto.of(report, reporter, targetUser);

    given(reportQuery.getReportById(anyLong())).willReturn(report);
    given(userQuery.getUserById(anyLong())).willReturn(reporter);
    given(userQuery.getUserById(anyLong())).willReturn(targetUser);

    // when
    ReportDetailDto result = reportService.findReportById(report.getId());

    // then
    assertAll(
        () -> AssertionsForClassTypes.assertThat(result.targetedNickname())
            .isEqualTo(response.targetedNickname()),
        () -> AssertionsForClassTypes.assertThat(result.reason()).isEqualTo(response.reason()),
        () -> AssertionsForClassTypes.assertThat(result.reasonDetail())
            .isEqualTo(response.reasonDetail())
    );
  }

  @DisplayName("모든 신고 내역 조회 시, 한 채팅방에서 신고 대상이 동일하다면 그룹화되어 하나만 조회된다.")
  @Test
  void findAllReportsTest() {
    // given
    Long lastReportId = 0L;
    int pageSize = 10;
    User targetUser = user();
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    Long targetedId = targetUser.getId();
    Long chattingRoomId = chattingRoom.getId();

    Report report1 = Report.builder()
        .reporterId(1L).chattingRoomId(chattingRoomId).targetedId(targetedId).reason("잠수")
        .reasonDetail("신고 상세 사유 입니다.")
        .build();
    Report report2 = Report.builder()
        .reporterId(2L).chattingRoomId(chattingRoomId).targetedId(targetedId).reason("잠수")
        .reasonDetail("신고 상세 사유 입니다.")
        .build();

    List<Report> reports = List.of(report1, report2);

    given(reportQuery.getAllReports(lastReportId, pageSize)).willReturn(reports);

    Set<Long> userIds = Set.of(targetedId);
    given(userQuery.getUsersByIdSet(userIds)).willReturn(Set.of(targetUser));

    Set<Long> chattingRoomIds = Set.of(chattingRoomId);
    given(chattingRoomQuery.getChattingRoomsSetBy(chattingRoomIds)).willReturn(
        Set.of(chattingRoom));

    // when
    ReportListDto responses = reportService.findAllReports(lastReportId, pageSize);

    // then
    assertAll(
        () -> assertThat(responses.contents()).hasSize(reports.size()),
        () -> assertThat(responses.contents()).allMatch(
            response -> response.targetedNickname().equals(targetUser.getProfile().getNickname()) &&
                response.chattingRoomName().equals(chattingRoom.getName()))
    );
  }

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
    List<GroupReportDto> response = reportService.findReportByTargetIdAndChattingRoomId(
        targetId, chattingRoomId);

    // given
    assertThat(response).hasSize(reports.size());
  }

}
