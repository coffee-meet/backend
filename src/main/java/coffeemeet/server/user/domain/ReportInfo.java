package coffeemeet.server.user.domain;

import coffeemeet.server.report.domain.ReportAmount;
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

  private LocalDateTime sanctionPeriod;

  public ReportInfo() {
    this.reportedCount = REPORT_MIN_COUNT;
    this.sanctionPeriod = null;
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
    this.sanctionPeriod = this.sanctionPeriod.plusDays(period);
  }

  private void permanentBan() {
    this.sanctionPeriod = LocalDateTime.MAX;
  }

}
