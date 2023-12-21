package coffeemeet.server.certification.implement;

import static coffeemeet.server.certification.exception.CertificationErrorCode.INVALID_VERIFICATION_CODE;

import coffeemeet.server.common.execption.InvalidInputException;
import org.springframework.stereotype.Component;

@Component
public class VerificationCodeValidator {

  private static final String WRONG_VERIFICATION_CODE_MESSAGE = "잘못된 인증코드(%s)를 입력했습니다.";

  public void validateVerificationCode(String verificationCode, String userInputCode) {
    if (!userInputCode.equals(verificationCode)) {
      throw new InvalidInputException(INVALID_VERIFICATION_CODE,
          String.format(WRONG_VERIFICATION_CODE_MESSAGE, verificationCode));
    }
  }

}
