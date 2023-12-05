package coffeemeet.server.user.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import coffeemeet.server.common.execption.MissMatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  @DisplayName("이메일을 생성할 수 있다.")
  void emailTest() {
    // when, then
    assertThatCode(Email::new).doesNotThrowAnyException();
    assertThatCode(() -> new Email("test123@gmail.com")).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("이메일을 생성 시 입력받은 값이 null일 경우 예외를 던진다.")
  void email_NullPointerExceptionTest() {
    // when, then
    assertThatThrownBy(() -> new Email(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("올바르지 않은 형식의 이메일을 생성할 경우 예외를 던진다.")
  void email_MissMatchExceptionTest() {
    // when, then
    assertThatThrownBy(() -> new Email("test123gmail.com"))
        .isInstanceOf(MissMatchException.class);
  }

  @Test
  @DisplayName("빈 칸의 이메일을 생성할 경우 예외를 던진다.")
  void emailBlank_MissMatchExceptionTest() {
    // when, then
    assertThatThrownBy(() -> new Email(" "))
        .isInstanceOf(MissMatchException.class);
  }

}
