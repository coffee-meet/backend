package coffeemeet.server.auth.exception;

import coffeemeet.server.common.execption.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
  INVALID_LOGIN_TYPE("A000", "지원하지 않는 로그인 타입입니다."),
  AUTHENTICATION_FAILED("A001", "인증이 실패했습니다."),
  EXPIRED_TOKEN("A002", "인증이 실패했습니다."),
  AUTHORIZATION_FAILED("A003", "인가에 실패했습니다."),
  HEADER_NOT_FOUND("A004", "헤더에 인증 코드가 없습니다."),
  ALREADY_REGISTERED("A009", "이미 가입된 사용자입니다."),
  DELETED_USER("A010", "탈퇴한 지 30일이 지난 사용자입니다. 다시 회원가입해 주세요.");

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
