package coffeemeet.server.report.domain;

import java.util.Arrays;

public enum ReportReason {
  FIRST("혐오 발언"),
  SECOND("괴롭힘 또는 스토킹"),
  THIRD("성적인 괴롭힘"),
  FOURTH("스팸 및 광고"),
  FIFTH("부적절한 콘텐츠"),
  SIXTH("잠수");

  private final String reason;

  ReportReason(String reason) {
    this.reason = reason;
  }

  public static boolean checkReason(String reason) {
    return Arrays.stream(ReportReason.values())
        .anyMatch(reportReason -> reportReason.reason.equals(reason));
  }

}
