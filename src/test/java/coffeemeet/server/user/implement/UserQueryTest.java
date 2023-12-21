package coffeemeet.server.user.implement;

import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    User user = user();

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
    User user1 = user();
    User user2 = user();
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
    User user1 = user();
    User user2 = user();
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
    User user = user();

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

  @Test
  @DisplayName("등록된 회원인지 확인할 수 있다.")
  void isRegisteredTest() {
    // given
    User user = user();
    OAuthInfo oauthInfo = user.getOauthInfo();

    given(userRepository.existsUserByOauthInfo(any())).willReturn(Boolean.TRUE);

    // when, then
    assertTrue(userQuery.isRegistered(oauthInfo));
  }

  @Test
  @DisplayName("아이디로 등록되지 않은 회원을 가져올 수 있다.")
  void getNonRegisteredUserByIdTest() {
    // given
    User user = user();

    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

    // when, then
    assertThatThrownBy(() -> userQuery.getNonRegisteredUserById(user.getId()))
        .isInstanceOf(NotFoundException.class);
  }

  @Test
  @DisplayName("채팅방에 참여하는 유저들을 가져올 수 있다.")
  void getUsersByRoomTest() {
    // given
    User user = user(UserStatus.MATCHING);
    User user1 = user(UserStatus.MATCHING);
    ChattingRoom chattingRoom = new ChattingRoom();

    user.completeMatching(chattingRoom);
    user.enterChattingRoom();
    user1.completeMatching(chattingRoom);
    user1.enterChattingRoom();

    given(userRepository.findAllByChattingRoom(any())).willReturn(List.of(user, user1));

    // when
    List<User> foundUsers = userQuery.getUsersByRoom(chattingRoom);

    // then
    assertThat(foundUsers)
        .hasSize(2)
        .contains(user, user1);
  }

  @Test
  @DisplayName("유저 아이디로 해당 유저들의 알림 정보를 가져올 수 있다.")
  void getNotificationInfoByUserIdTest() {
    // given
    User user = user();
    NotificationInfo notificationInfo = user.getNotificationInfo();

    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

    // when
    NotificationInfo result = userQuery.getNotificationInfoByUserId(user.getId());

    // then
    assertThat(result.getToken()).isEqualTo(notificationInfo.getToken());
    assertThat(result.getCreatedTokenAt()).isEqualTo(notificationInfo.getCreatedTokenAt());
  }

}
