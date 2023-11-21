package coffeemeet.server.user.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserQueryTest {

  @InjectMocks
  private UserQuery userQuery;

  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("유저 아이디로 해당 유저를 가져올 수 있다.")
  void getUserByIdTest() {
    // given
    Long userId = 1L;
    User user = UserFixture.user();

    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

    // when
    User foundUser = userQuery.getUserById(userId);

    // then
    assertThat(foundUser).isEqualTo(user);
  }

  @Test
  @DisplayName("유저들의 아이디로 해당 유저 Set을 가져올 수 있다.")
  void getUsersByIdSetTest() {
    // given
    User user1 = UserFixture.user();
    User user2 = UserFixture.user();
    Set<Long> userIds = new HashSet<>(Arrays.asList(1L, 2L));
    Set<User> users = new HashSet<>(Arrays.asList(user1, user2));

    given(userRepository.findByIdIn(any())).willReturn(users);

    // when
    Set<User> foundUsers = userQuery.getUsersByIdSet(userIds);

    // then
    assertThat(foundUsers).isEqualTo(users);
  }

  @Test
  @DisplayName("유저들의 아이디로 해당 유저들의 알림 정보 Set을 가져올 수 있다.")
  void getNotificationInfosByIdSetTest() {
    // given
    User user1 = UserFixture.user();
    User user2 = UserFixture.user();
    Set<Long> userIds = new HashSet<>(Arrays.asList(1L, 2L));
    Set<User> users = new HashSet<>(Arrays.asList(user1, user2));

    given(userRepository.findByIdIn(userIds)).willReturn(users);

    Set<NotificationInfo> notificationInfos = users.stream().map(User::getNotificationInfo)
        .collect(Collectors.toSet());

    // when
    Set<NotificationInfo> foundNotificationInfos = userQuery.getNotificationInfosByIdSet(userIds);

    // then
    assertThat(foundNotificationInfos).isEqualTo(notificationInfos);
  }

  @Test
  @DisplayName("로그인 정보와 로그인 아이디로 해당 유저를 찾을 수 있다.")
  void getUserByOAuthInfoTest() {
    // given
    User user = UserFixture.user();

    given(userRepository.findByOauthInfo(any())).willReturn(Optional.of(user));

    // when
    User foundUser = userQuery.getUserByOAuthInfo(user.getOauthInfo());

    // then
    assertThat(user).isEqualTo(foundUser);
  }

  @Test
  @DisplayName("닉네임을 중복체크할 수 있다.")
  void hasDuplicatedNicknameTest() {
    // given
    String nickname = "nickname";

    given(userRepository.existsUserByProfile_Nickname(any())).willReturn(Boolean.FALSE);

    // when, then
    assertThatCode(() -> userQuery.hasDuplicatedNickname(nickname))
        .doesNotThrowAnyException();
  }

}
