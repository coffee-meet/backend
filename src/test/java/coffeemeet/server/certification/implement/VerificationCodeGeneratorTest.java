package coffeemeet.server.certification.implement;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VerificationCodeGeneratorTest {

  private final VerificationCodeGenerator verificationCodeGenerator = new VerificationCodeGenerator();

  @Test
  @DisplayName("6자리의 랜덤 인증 코드를 생성할 수 있다.")
  void generateVerificationCodeTest() {
    // when
    String code = verificationCodeGenerator.generateVerificationCode();

    // then
    assertThat(code).matches("[0-9]{6}");
  }

}
