package coffeemeet.server.user.domain;

import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InterestTest {

  @Test
  @DisplayName("관심사를 생성할 수 있다.")
  void interestTest() {
    // given
    User user = user();

    // when, then
    assertThatCode(Interest::new).doesNotThrowAnyException();
    assertThatCode(() -> new Interest(Keyword.게임, user)).doesNotThrowAnyException();
  }

}
