package coffeemeet.server.chatting.exception;

import coffeemeet.server.common.execption.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChattingErrorCode implements ErrorCode {
  INVALID_MESSAGE("CM000", "유효하지 않은 메세지 형식입니다."),
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
