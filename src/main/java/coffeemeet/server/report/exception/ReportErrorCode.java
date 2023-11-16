package coffeemeet.server.report.exception;

import coffeemeet.server.common.execption.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReportErrorCode implements ErrorCode {
  EXCEEDED_MAX_REPORT_COUNT("R000", "이미 영구 정지된 사용자입니다."),
  REPORT_NOT_FOUND("R004", "해당 신고 정보를 찾을 수가 없습니다.")
  ;

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
