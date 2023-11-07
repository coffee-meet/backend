package coffeemeet.server.user.implement;

import static coffeemeet.server.common.fixture.entity.UserFixture.token;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.InterestRepository;
import coffeemeet.server.user.infrastructure.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserCommandTest {

  @InjectMocks
  private UserCommand userCommand;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserQuery userQuery;

  @Mock
  private InterestRepository interestRepository;

  @Test
  @DisplayName("알림 정보를 등록하거나 업데이트할 수 있다.")
  void registerOrUpdateNotificationToken() {
    // given
    User user = user();
    String token = token();
    given(userQuery.getUserById(user.getId())).willReturn(user);

    // when
    userCommand.registerOrUpdateNotificationToken(user.getId(), token);

    // then
    assertThat(user.getNotificationInfo().getToken()).isEqualTo(token);
  }

  @Test
  @DisplayName("푸시 알림을 거부할 수 있다.")
  void unsubscribeNotification() {
    User user = user();
    given(userQuery.getUserById(user.getId())).willReturn(user);

    // when
    userCommand.unsubscribeNotification(user.getId());

    // then
    assertThat(user.getNotificationInfo().isSubscribedToNotification()).isFalse();
  }

}
