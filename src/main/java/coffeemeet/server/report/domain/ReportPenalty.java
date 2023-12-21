package coffeemeet.server.report.domain;

import static coffeemeet.server.common.execption.GlobalErrorCode.INTERNAL_SERVER_ERROR;

import coffeemeet.server.common.execption.CoffeeMeetException;
import lombok.Getter;

@Getter
public enum ReportPenalty {
  ONE_TIME(1, 3),
  TWO_TIMES(2, 7),
  THREE_TIMES(3, 15),
  FOUR_TIMES(4, 30),
  FIVE_TIMES(5, -1); // -1은 영구 정지를 의미합니다.

  private static final String INVALID_REPORT_COUNT = "잘못된 신고 횟수(%s) 입니다.";

  private final int reportCount;
  private final int penaltyDuration;

  ReportPenalty(int reportCount, int penaltyDuration) {
    this.reportCount = reportCount;
    this.penaltyDuration = penaltyDuration;
  }

  public static int getSanctionPeriodByReportCount(int reportCount) {
    for (ReportPenalty penalty : ReportPenalty.values()) {
      if (penalty.getReportCount() == reportCount) {
        return penalty.getPenaltyDuration();
      }
    }
    throw new CoffeeMeetException(
        INTERNAL_SERVER_ERROR, String.format(INVALID_REPORT_COUNT, reportCount)
    );
  }
}
