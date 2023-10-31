package coffeemeet.server.certification.exception;

import coffeemeet.server.common.execption.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CertificationErrorCode implements ErrorCode {
  INVALID_VERIFICATION_CODE("C004", "잘못된 인증코드입니다.");

  private final String errorCode;
  private final String message;

  @Override
  public String code() {
    return null;
  }

  @Override
  public String message() {
    return null;
  }

}
