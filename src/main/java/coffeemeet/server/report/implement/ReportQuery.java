package coffeemeet.server.report.implement;

import static coffeemeet.server.report.exception.ReportErrorCode.ALREADY_EXIST_REPORT;

import coffeemeet.server.common.execption.DuplicatedDataException;
import coffeemeet.server.report.infrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportQuery {

  private static final String ALREADY_REPORTED_MESSAGE = "해당 사용자(%s)의 채팅방(%s)의 사용자(%s)에 대한 신고가 이미 존재합니다.";
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

}
