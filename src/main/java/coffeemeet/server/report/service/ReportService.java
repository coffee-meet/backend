package coffeemeet.server.report.service;

import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.report.implement.ReportQuery;
import coffeemeet.server.report.service.dto.AllReportDto;
import coffeemeet.server.report.service.dto.ReportDto;
import coffeemeet.server.report.service.dto.TargetReportDto;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final ReportCommand reportCommand;
  private final ReportQuery reportQuery;
  private final UserQuery userQuery;
  private final ChattingRoomQuery chattingRoomQuery;

  @Transactional
  public void reportUser(long reporterId, long chattingRoomId, long targetId, String reason,
      String reasonDetail) {
    reportQuery.hasDuplicatedReport(reporterId, chattingRoomId, targetId);

    chattingRoomQuery.existsById(chattingRoomId);

    Report report = Report.builder()
        .reporterId(reporterId)
        .chattingRoomId(chattingRoomId)
        .targetId(targetId)
        .reason(reason)
        .reasonDetail(reasonDetail)
        .build();
    reportCommand.save(report);
  }

  public ReportDto.Response findReportById(Long reportId) {
    Report report = reportQuery.getReportById(reportId);
    User reporter = userQuery.getUserById(report.getReporterId());
    User targetUser = userQuery.getUserById(report.getTargetId());
    return ReportDto.Response.of(report, reporter, targetUser);
  }

  public List<AllReportDto.Response> findAllReports() {
    List<Report> allReports = reportQuery.getAllReports();
    return allReports.stream()
        .map(this::mapToAllReportDto)
        .toList();
  }

  public List<TargetReportDto.Response> findReportByTargetIdAndChattingRoomId(long targetId,
      long chattingRoomId) {
    List<Report> reports = reportQuery.getReportsByTargetIdAndChattingRoomId(targetId,
        chattingRoomId);
    return reports.stream()
        .map(this::mapToReportDto)
        .toList();
  }

  private AllReportDto.Response mapToAllReportDto(Report report) {
    User targetUser = userQuery.getUserById(report.getTargetId());
    return AllReportDto.Response.of(targetUser);
  }

  private TargetReportDto.Response mapToReportDto(Report report) {
    User reporter = userQuery.getUserById(report.getReporterId());
    return TargetReportDto.Response.of(reporter.getProfile().getNickname(), report.getCreatedAt());
  }

}
