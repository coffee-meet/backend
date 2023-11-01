package coffeemeet.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Embeddable
public class ReportInfo {

  private static final int REPORT_MIN_COUNT = 0;

  @Column(nullable = false)
  private int reportedCount;

  private LocalDateTime sanctionPeriod;

  public ReportInfo() {
    this.reportedCount = REPORT_MIN_COUNT;
    this.sanctionPeriod = null;
  }

}
