package coffeemeet.server.inquiry.exception;


import coffeemeet.server.common.execption.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryErrorCode implements ErrorCode {
  NOT_EXIST_INQUIRY("U004", "존재하지 않는 문의입니다"),
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
