package coffeemeet.server.user.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import coffeemeet.server.common.execption.DataLengthExceededException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BirthTest {

  @Test
  @DisplayName("생년월일을 생성할 수 있다.")
  void birthTest() {
    // when, then
    assertThatCode(Birth::new).doesNotThrowAnyException();
    assertThatCode(() -> new Birth("2001", "1018")).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("생일날짜와 년도가 올바르지 않을 경우 예외를 던진다.")
  void birth_DataLengthExceededExceptionTest() {
    // when, then
    assertAll(
        () -> assertThatThrownBy(() -> new Birth("2001", "12345")).isInstanceOf(
            DataLengthExceededException.class),
        () -> assertThatThrownBy(() -> new Birth("20011", "1018")).isInstanceOf(
            DataLengthExceededException.class),
        () -> assertThatThrownBy(() -> new Birth(" ", "1018")).isInstanceOf(
            DataLengthExceededException.class),
        () -> assertThatThrownBy(() -> new Birth("2001", " ")).isInstanceOf(
            DataLengthExceededException.class)
    );
  }

  @Test
  @DisplayName("생일년도가 올바르지 않을 경우 예외를 던진다.")
  void birth_NullPointerExceptionTest() {
    // when, then
    assertThatThrownBy(() -> new Birth(null, "1018")).isInstanceOf(
        NullPointerException.class);
    assertThatThrownBy(() -> new Birth("2001", null)).isInstanceOf(
        NullPointerException.class);
  }

}
