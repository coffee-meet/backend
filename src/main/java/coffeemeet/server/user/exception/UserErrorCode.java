package coffeemeet.server.user.exception;

import coffeemeet.server.common.execption.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
  INVALID_NICKNAME("U000", "올바르지 않은 닉네임입니다."),
  INVALID_NAME("U000", "올바르지 않은 이름입니다."),
  INVALID_PROFILE_IMAGE("U000", "올바르지 않은 프로필 사진입니다."),
  INVALID_EMAIL("U000", "올바르지 않은 이메입니다."),
  INVALID_BIRTH_YEAR("U000", "올바르지 않은 출생년도입니다."),
  INVALID_BIRTH_DAY("U000", "올바르지 않은 날짜형입니다."),
  NOT_EXIST_USER("U004", "존재하지 않는 사용자입니다"),
  ALREADY_REGISTERED_USER("U004", "이미 회원가입된 사용자 입니다."),
  ALREADY_EXIST_NICKNAME("U009", "이미 존재하는 닉네임입니다."),
  ALREADY_EXIST_USER("U009", "이미 존재하는 사용자입니다.");

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
