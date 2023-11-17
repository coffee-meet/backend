package coffeemeet.server.report.domain;

import static coffeemeet.server.report.exception.ReportErrorCode.INVALID_REASON;

import coffeemeet.server.common.execption.InvalidInputException;
import java.util.Arrays;

public enum ReportReason {
  HATE_SPEECH("혐오 발언"),
  HARASSMENT_OR_STALKING("괴롭힘 또는 스토킹"),
  SEXUAL_HARASSMENT("성적인 괴롭힘"),
  SPAM_AND_ADVERTISING("스팸 및 광고"),
  INAPPROPRIATE_CONTENT("부적절한 콘텐츠"),
  INACTIVE("잠수");

  private static final String INVALID_REASON_MESSAGE = "해당 신고 사유(%s)는 유효하지 않습니다.";
  private final String reason;

  ReportReason(String reason) {
    this.reason = reason;
  }

  public static ReportReason getReason(String reason) {
    return Arrays.stream(ReportReason.values())
        .filter(reportReason -> reportReason.reason.equals(reason))
        .findAny()
        .orElseThrow(() -> new InvalidInputException(
            INVALID_REASON,
            String.format(INVALID_REASON_MESSAGE, reason))
        );
  }

}
