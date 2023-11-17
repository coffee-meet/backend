package coffeemeet.server.report.domain;

import static coffeemeet.server.report.exception.ReportErrorCode.INVALID_REASON;

import coffeemeet.server.common.execption.InvalidInputException;

public enum ReportReason {
  혐오_발언,
  괴롭힘_또는_스토킹,
  성적인_괴롭힘,
  스팸_및_광고,
  부적절한_콘텐츠,
  잠수;

  private static final String INVALID_REASON_MESSAGE = "해당 신고 사유(%s)는 유효하지 않습니다.";

  public static ReportReason getReason(String reason) {
    return switch (reason) {
      case "혐오 발언", "혐오_발언" -> 혐오_발언;
      case "괴롭힘 또는 스토킹", "괴롭힘_또는_스토킹" -> 괴롭힘_또는_스토킹;
      case "성적인 괴롭힘", "성적인_괴롭힘" -> 성적인_괴롭힘;
      case "스팸 및 광고", "스팸_및_광고" -> 스팸_및_광고;
      case "부적절한 콘텐츠", "부적절한_콘텐츠" -> 부적절한_콘텐츠;
      case "잠수" -> 잠수;
      default -> throw new InvalidInputException(
          INVALID_REASON,
          String.format(INVALID_REASON_MESSAGE, reason));
    };
  }

}
