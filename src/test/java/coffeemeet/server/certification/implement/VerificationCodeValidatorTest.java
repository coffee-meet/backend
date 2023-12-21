package coffeemeet.server.certification.implement;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import coffeemeet.server.common.execption.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VerificationCodeValidatorTest {

  private final VerificationCodeValidator verificationCodeValidator = new VerificationCodeValidator();

  @Test
  @DisplayName("일치하는 인증 코드가 입력되면 예외가 발생하지 않는다")
  void validateVerificationCodeTest() {
    // given
    String verificationCode = "123456";
    String userInputCode = "123456";

    // when, then
    assertThatCode(
        () -> verificationCodeValidator.validateVerificationCode(verificationCode, userInputCode)
    ).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("일치하지 않는 인증 코드가 입력되면 예외가 발생한다")
  void validateVerificationCode_WithIncorrectCode_ShouldThrowException() {
    // given
    String verificationCode = "123456";
    String userInputCode = "654321";

    // when, then
    assertThatThrownBy(
        () -> verificationCodeValidator.validateVerificationCode(verificationCode, userInputCode))
        .isInstanceOf(InvalidInputException.class);
  }

}
