package coffeemeet.server.report.domain;

import static coffeemeet.server.report.exception.ReportErrorCode.INVALID_REPORTED_COUNT;

import coffeemeet.server.common.execption.InvalidInputException;
import java.util.Arrays;

public enum ReportAmount {
  ONE_TIME(1, 3),
  TWO_TIMES(2, 5),
  THREE_TIMES(3, 7),
  FOUR_TIMES(4, 30),
  FIVE_TIMES(5, -1);

  private static final String INVALID_REPORTED_COUNT_MESSAGE = "해당 신고 횟수(%s)는 유효하지 않습니다.";

  private final int reportedCount;
  private final int sanctionPeriod;

  ReportAmount(int reportedCount, int sanctionPeriod) {
    this.reportedCount = reportedCount;
    this.sanctionPeriod = sanctionPeriod;
  }

  public static int getSanctionPeriod(int reportedCount) {
    return Arrays.stream(ReportAmount.values())
        .filter(detail -> detail.reportedCount == reportedCount)
        .findAny()
        .map(detail -> detail.sanctionPeriod)
        .orElseThrow(() -> new InvalidInputException(
            INVALID_REPORTED_COUNT,
            String.format(INVALID_REPORTED_COUNT_MESSAGE, reportedCount))
        );
  }

}
