package coffeemeet.server.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import coffeemeet.server.common.execption.DataLengthExceededException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProfileTest {

  @Test
  @DisplayName("올바르지 않은 닉네임일 경우 예외를 던진다.")
  void nickname_DataLengthExceededExceptionTest() {
    // when, then
    assertThatThrownBy(() -> new Profile("123451234512345123451243")).isInstanceOf(
        DataLengthExceededException.class);
    assertThatThrownBy(() -> new Profile(" ")).isInstanceOf(
        DataLengthExceededException.class);
  }

  @Test
  @DisplayName("닉네임 생성 시 입력받은 값이 null일 경우 예외를 던진다.")
  void nickname_NullPointerExceptionTest() {
    // when, then
    assertThatThrownBy(() -> new Profile(null)).isInstanceOf(
        NullPointerException.class);
  }

  @Test
  @DisplayName("닉네임을 변경할 수 있다.")
  void updateNicknameTest() {
    // given
    String beforeNickname = "beforeNickname";
    String afterNickname = "afterNickname";
    Profile profile = new Profile(beforeNickname);

    // when
    profile.updateNickname(afterNickname);

    // then
    assertThat(profile.getNickname()).isNotEqualTo(beforeNickname);
    assertThat(profile.getNickname()).isEqualTo(afterNickname);
  }

}
