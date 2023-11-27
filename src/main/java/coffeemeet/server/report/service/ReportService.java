package coffeemeet.server.report.service;

import static coffeemeet.server.chatting.exception.ChattingErrorCode.CHATTING_ROOM_NOT_FOUND;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.history.implement.UserChattingHistoryQuery;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.report.implement.ReportQuery;
import coffeemeet.server.report.presentation.dto.ReportList;
import coffeemeet.server.report.service.dto.GroupReportDto;
import coffeemeet.server.report.service.dto.ReportDetailDto;
import coffeemeet.server.report.service.dto.ReportDto;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

  private static final String CHATTING_ROOM_NOT_FOUND_MESSAGE = "(%s)번 채팅방을 찾을 수 없습니다.";

  private final ReportCommand reportCommand;
  private final ReportQuery reportQuery;
  private final UserQuery userQuery;
  private final ChattingRoomQuery chattingRoomQuery;
  private final UserChattingHistoryQuery userChattingHistoryQuery;

  @Transactional
  public void reportUser(long reporterId, long chattingRoomId, long targetId, String reason,
      String reasonDetail) {
    reportQuery.hasDuplicatedReport(reporterId, chattingRoomId, targetId);

    checkChattingRoomExists(reporterId, chattingRoomId);

    Report report = Report.builder()
        .reporterId(reporterId)
        .chattingRoomId(chattingRoomId)
        .targetedId(targetId)
        .reason(reason)
        .reasonDetail(reasonDetail)
        .build();
    reportCommand.createReport(report);
  }

  public ReportDetailDto findReportById(Long reportId) {
    Report report = reportQuery.getReportById(reportId);
    User reporter = userQuery.getUserById(report.getReporterId());
    User targetUser = userQuery.getUserById(report.getTargetedId());
    return ReportDetailDto.of(report, reporter, targetUser);
  }

  public ReportList findAllReports(Long lastReportId, int pageSize) {
    List<Report> reports = reportQuery.getAllReports(lastReportId, pageSize);
    boolean hasNext = reports.size() >= pageSize;

    Map<Long, User> userMap = getUsers(reports);
    Map<Long, ChattingRoom> chattingRoomMap = getChattingRooms(reports);

    List<ReportDto> responses = reports.stream()
        .map(report -> {
          User targetUser = userMap.get(report.getTargetedId());
          ChattingRoom chattingRoom = chattingRoomMap.get(report.getChattingRoomId());
          return ReportDto.of(targetUser, chattingRoom);
        })
        .toList();
    return ReportList.of(responses, hasNext);
  }

  public List<GroupReportDto> findReportByTargetIdAndChattingRoomId(long targetId,
      long chattingRoomId) {
    List<Report> reports = reportQuery.getReportsByTargetIdAndChattingRoomId(targetId,
        chattingRoomId);
    return reports.stream()
        .map(this::mapToReportDto)
        .toList();
  }

  private void checkChattingRoomExists(long reporterId, long chattingRoomId) {
    boolean isChattingRoomExists = false;
    boolean isChattingHistoryExists = false;

    try {
      chattingRoomQuery.existsById(chattingRoomId);
      isChattingRoomExists = true;
    } catch (NotFoundException e) {
      isChattingHistoryExists = userChattingHistoryQuery.existsByUserId(reporterId);
    }

    if (!isChattingRoomExists && !isChattingHistoryExists) {
      throw new NotFoundException(
          CHATTING_ROOM_NOT_FOUND,
          String.format(CHATTING_ROOM_NOT_FOUND_MESSAGE, chattingRoomId));
    }
  }

  private Map<Long, ChattingRoom> getChattingRooms(List<Report> allReports) {
    Set<Long> chattingRoomIds = allReports.stream()
        .map(Report::getChattingRoomId)
        .collect(Collectors.toSet());
    Set<ChattingRoom> chattingRooms = chattingRoomQuery.getUserByIdSet(chattingRoomIds);
    return chattingRooms.stream()
        .collect(Collectors.toMap(ChattingRoom::getId, Function.identity()));
  }

  private Map<Long, User> getUsers(List<Report> allReports) {
    Set<Long> targetUserIds = allReports.stream()
        .map(Report::getTargetedId)
        .collect(Collectors.toSet());
    Set<User> users = userQuery.getUsersByIdSet(targetUserIds);
    return users.stream()
        .collect(Collectors.toMap(User::getId, Function.identity()));
  }

  private GroupReportDto mapToReportDto(Report report) {
    User reporter = userQuery.getUserById(report.getReporterId());
    return GroupReportDto.of(reporter.getProfile().getNickname(), report.getCreatedAt());
  }

}
