package coffeemeet.server.report.exception;

import coffeemeet.server.common.execption.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements ErrorCode {
  INVALID_REPORTED_COUNT("R000","유효하지 않은 신고 횟수입니다."),
  INVALID_REASON("R000","유효하지 않은 신고 사유입니다."),
  INVALID_REASON_DETAIL("R000","유효하지 않은 신고 상세 사유입니다."),
  INVALID_REPORTER("R000", "유효하지 않은 신고자입니다."),
  INVALID_CHATTING_ROOM("R000", "유효하지 않은 채팅방입니다."),
  INVALID_TARGET_USER("R000", "유효하지 않은 신고 대상입니다."),
  REPORT_NOT_FOUND("R004", "신고 내역을 찾을 수 없습니다.");

  private final String errorCode;
  private final String message;

  @Override
  public String code() {
    return errorCode;
  }

  @Override
  public String message() {
    return message;
  }

}
