package coffeemeet.server.report.implement;

import static coffeemeet.server.report.exception.ReportErrorCode.ALREADY_EXIST_REPORT;
import static coffeemeet.server.report.exception.ReportErrorCode.REPORT_NOT_FOUND;

import coffeemeet.server.common.execption.DuplicatedDataException;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.infrastructure.ReportRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportQuery {

  private static final String ALREADY_REPORTED_MESSAGE = "해당 사용자(%s)의 채팅방(%s)의 사용자(%s)에 대한 신고가 이미 존재합니다.";
  private static final String REPORT_NOT_FOUND_MESSAGE = "해당 아이디(%s)에 대한 신고 내역을 찾을 수 없습니다.";

  private final ReportRepository reportRepository;

  public void hasDuplicatedReport(long userId, long chattingRoomId, long targetId) {
    if (reportRepository.existsByReporterIdAndChattingRoomIdAndTargetId(userId, chattingRoomId,
        targetId)) {
      throw new DuplicatedDataException(
          ALREADY_EXIST_REPORT,
          String.format(ALREADY_REPORTED_MESSAGE,
              userId, chattingRoomId, targetId)
      );
    }
  }

  public Report getReportById(long reportId) {
    return reportRepository.findById(reportId)
        .orElseThrow(() -> new NotFoundException(
                REPORT_NOT_FOUND,
                String.format(REPORT_NOT_FOUND_MESSAGE, reportId)
            )
        );
  }

  public List<Report> getReportsByTargetIdAndChattingRoomId(long targetId, long chattingRoomId) {
    List<Report> reports = reportRepository.findByTargetIdAndChattingRoomId(targetId, chattingRoomId);
    if (reports.isEmpty()) {
      throw new NotFoundException(
          REPORT_NOT_FOUND,
          String.format(REPORT_NOT_FOUND_MESSAGE, targetId)
      );
    }
    return reports;
  }

  public List<Report> getAllReports() {
    return reportRepository.findAllReportsGroupedByTargetIdAndChattingRoomId();
  }

}
