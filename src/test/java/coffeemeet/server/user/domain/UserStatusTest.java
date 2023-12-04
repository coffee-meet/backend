package coffeemeet.server.user.domain;

import static coffeemeet.server.user.domain.UserStatus.CHATTING_CONNECTED;
import static coffeemeet.server.user.domain.UserStatus.CHATTING_UNCONNECTED;
import static coffeemeet.server.user.domain.UserStatus.IDLE;
import static coffeemeet.server.user.domain.UserStatus.MATCHING;
import static coffeemeet.server.user.domain.UserStatus.REPORTED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserStatusTest {

  @Test
  @DisplayName("유저 상들을 가져올 수 있다.")
  void valuesTest() {
    // when
    UserStatus[] userStatuses = UserStatus.values();

    // then
    assertThat(userStatuses).hasSize(5);
    assertThat(userStatuses).contains(IDLE, MATCHING, CHATTING_CONNECTED, CHATTING_UNCONNECTED,
        REPORTED);
  }

  @Test
  @DisplayName("해당 문자열에 대한 유저 상태를 가져올 수 있다.")
  void valueOfTest() {
    // when
    UserStatus userStatus = UserStatus.valueOf("MATCHING");

    // then
    assertThat(userStatus).isEqualTo(MATCHING);
  }

}
