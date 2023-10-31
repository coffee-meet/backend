package coffeemeet.server.certification.exception;

import coffeemeet.server.common.execption.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CertificationErrorCode implements ErrorCode {
  EXISTED_COMPANY_EMAIL("C000", "이메일이 중복됩니다."),
  CERTIFICATION_NOT_FOUND("C004", "인증 정보를 찾을 수 없습니다."),
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
