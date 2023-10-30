package coffeemeet.server.common.execption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

  VALIDATION_ERROR("G000", "유효하지 않은 입력입니다."),
  INTERNAL_SERVER_ERROR("G050", "예상치 못한 오류입니다.");

  private final String code;
  private final String message;

  @Override
  public String code() {
    return this.code;
  }

  @Override
  public String message() {
    return this.message;
  }

}
