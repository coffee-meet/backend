package coffeemeet.server.chatting.current.implement;

import static coffeemeet.server.common.fixture.UserFixture.fourUsers;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import coffeemeet.server.common.execption.ForbiddenException;
import coffeemeet.server.user.domain.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingRoomUserValidatorTest {

  @InjectMocks
  private ChattingRoomUserValidator chattingRoomUserValidator;

  @Nested
  @DisplayName("채팅방 내에 있는 유저인지 검증할 수 있다.")
  class ValidateUserInChattingRoom {

    @Test
    void success() {
      // given
      List<User> users = fourUsers();
      Long requestUserId = users.get(0).getId();

      // when, then
      assertThatCode(
          () -> chattingRoomUserValidator.validateUserInChattingRoom(requestUserId, users))
          .doesNotThrowAnyException();
    }

    @Test
    void forbiddenException() {
      // given
      List<User> users = fourUsers();
      Long requestUserId = generateNonExistingUserId(users);

      // when, then
      assertThrows(ForbiddenException.class,
          () -> chattingRoomUserValidator.validateUserInChattingRoom(requestUserId, users));
    }

    private Long generateNonExistingUserId(List<User> users) {
      Set<Long> existingUserIds = new HashSet<>();
      users.forEach(user -> existingUserIds.add(user.getId()));

      RandomGenerator random = RandomGenerator.getDefault();
      long nonExistingUserId;
      do {
        nonExistingUserId = random.nextLong();
      } while (existingUserIds.contains(nonExistingUserId));

      return nonExistingUserId;
    }

  }

}
