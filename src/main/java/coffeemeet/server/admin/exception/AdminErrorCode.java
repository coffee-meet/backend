package coffeemeet.server.admin.exception;

import coffeemeet.server.common.execption.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements ErrorCode {
  INVALID_LOGIN_REQUEST("A001", "잘못된 아이디 비밀번호 입니다."),
  NOT_AUTHORIZED("A001", "해당 요청에 대한 권한이 없습니다.");

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
