package coffeemeet.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportInfo {

  private static final int REPORT_MIN_COUNT = 0;

  @Column(nullable = false)
  private int reportedCount;

  @Column(nullable = false)
  private LocalDateTime sanctionPeriod;

  public ReportInfo(int reportedCount, LocalDateTime sanctionPeriod) {
    validateReportedCount(reportedCount);
    validateSanctionPeriod(sanctionPeriod);
    this.reportedCount = reportedCount;
    this.sanctionPeriod = sanctionPeriod;
  }

  private void validateReportedCount(int reportedCount) {
    if (reportedCount < REPORT_MIN_COUNT) {
      throw new IllegalArgumentException("올바르지 않은 신고횟수입니다.");
    }
  }

  private void validateSanctionPeriod(LocalDateTime sanctionPeriod) {
    if (sanctionPeriod == null) {
      throw new IllegalArgumentException("올바르지 않은 제재 기간입니다.");
    }
  }

}
