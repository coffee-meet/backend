package coffeemeet.server.user.domain;

import static coffeemeet.server.report.exception.ReportErrorCode.EXCEEDED_MAX_REPORT_COUNT;

import coffeemeet.server.common.execption.LimitExceededException;
import coffeemeet.server.report.domain.ReportAmount;
import coffeemeet.server.report.domain.ReportPenalty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Embeddable
public class ReportInfo {

  private static final int REPORT_MIN_COUNT = 0;
  private static final int REPORT_MAX_COUNT = 5;

  @Column(nullable = false)
  private int reportedCount;

  private LocalDateTime penaltyExpiration;

  public ReportInfo() {
    this.reportedCount = REPORT_MIN_COUNT;
    this.penaltyExpiration = null;
  }

  public void increment() {
    if (reportedCount >= REPORT_MAX_COUNT) {
      throw new LimitExceededException(EXCEEDED_MAX_REPORT_COUNT,
          String.format("최대 신고 누적 횟수를 초과합니다. 신고 누적 횟수(%s)", reportedCount));
    }
    this.reportedCount++;
    int sanctionPeriodByReportCount = ReportPenalty.getSanctionPeriodByReportCount(reportedCount);
    if (sanctionPeriodByReportCount == -1) {
      penaltyExpiration = LocalDateTime.MAX;
    }
    penaltyExpiration = LocalDateTime.now().plusDays(sanctionPeriodByReportCount);
  }

  public void updateReportedCount(int reportedCount) {
    validateReportedCount(reportedCount);
    this.reportedCount += reportedCount;
    updateSanctionPeriod(this.reportedCount);
  }

  private void validateReportedCount(int reportedCount) {
    if (this.reportedCount + reportedCount >= REPORT_MAX_COUNT) {
      permanentBan();
    }
  }

  private void updateSanctionPeriod(int reportedCount) {
    int period = ReportAmount.getSanctionPeriod(reportedCount);
    this.penaltyExpiration = this.penaltyExpiration.plusDays(period);
  }

  private void permanentBan() {
    this.penaltyExpiration = LocalDateTime.MAX;
  }

}
