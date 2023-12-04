package coffeemeet.server.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import coffeemeet.server.common.execption.LimitExceededException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReportInfoTest {

  @Test
  @DisplayName("최대 신고 횟수를 초과할 경우 예외를 던진다.")
  void incrementTest() {
    ReportInfo reportInfo = new ReportInfo();
    for (int i = 0; i < 5; i++) {
      reportInfo.increment();
    }

    assertThatThrownBy(reportInfo::increment).isInstanceOf(LimitExceededException.class);
  }

  @Test
  @DisplayName("신고 횟수를 증가시킬 수 있다.")
  void updateReportedCountTest() {
    ReportInfo reportInfo = new ReportInfo();
    int beforeReportedCount = reportInfo.getReportedCount();
    int increaseAmount = 2;
    reportInfo.increment();
    int incrementedReportedCount = beforeReportedCount + 1;

    reportInfo.updateReportedCount(increaseAmount);

    assertThat(incrementedReportedCount + increaseAmount).isEqualTo(reportInfo.getReportedCount());
  }

  @Test
  @DisplayName("신고 횟수에 따라 제재 기간이 업데이트 된다.")
  void updatePenaltyExpirationTest() {
    ReportInfo reportInfo = new ReportInfo();
    reportInfo.increment();
    LocalDateTime beforeExpiration = reportInfo.getPenaltyExpiration();

    int incrementAmount = 2;
    reportInfo.updateReportedCount(incrementAmount);

    assertThat(reportInfo.getPenaltyExpiration()).isNotNull();
    assertThat(beforeExpiration).isNotEqualTo(reportInfo.getPenaltyExpiration());
  }

  @Test
  @DisplayName("신고 횟수가 최대 횟수를 초과한다면 영구 정지된다.")
  void permanentBanTest() {
    ReportInfo reportInfo = new ReportInfo();
    int increaseAmount = 5;

    reportInfo.updateReportedCount(increaseAmount);

    assertThat(reportInfo.getPenaltyExpiration().plusDays(1)).isEqualTo(LocalDateTime.MAX);
  }

}
