package coffeemeet.server.report.service;

import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.report.implement.ReportQuery;
import coffeemeet.server.report.service.dto.AllReportDto;
import coffeemeet.server.report.service.dto.ReportDto;
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
  public void reportUser(long userId, long chattingRoomId, long targetId, String reason,
      String reasonDetail) {
    reportQuery.hasDuplicatedReport(userId, chattingRoomId, targetId);

    chattingRoomQuery.existsById(chattingRoomId);
    User reporter = userQuery.getUserById(userId);

    Report report = Report.builder()
        .reporterId(userId)
        .chattingRoomId(chattingRoomId)
        .targetId(targetId)
        .reporterEmail(reporter.getProfile().getEmail())
        .reason(reason)
        .reasonDetail(reasonDetail)
        .build();
    reportCommand.save(report);
  }

  public ReportDto.Response findReport(Long reportId) {
    Report report = reportQuery.getReportById(reportId);
    User reporter = userQuery.getUserById(report.getReporterId());
    User targetUser = userQuery.getUserById(report.getTargetId());
    return ReportDto.Response.of(report, reporter, targetUser);
  }

  public List<AllReportDto.Response> findAllReports() {
    List<Report> allReports = reportQuery.getAllReports();
    return allReports.stream()
        .filter(report -> !report.isProcessed())
        .map(this::mapToAllReportDto)
        .toList();
  }

  private AllReportDto.Response mapToAllReportDto(Report report) {
    User targetUser = userQuery.getUserById(report.getTargetId());
    return AllReportDto.Response.of(targetUser);
  }

}
