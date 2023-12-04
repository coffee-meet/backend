package coffeemeet.server.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.common.execption.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  @DisplayName("유저를 생성할 수 있다.")
  void userTest() {
    // when, then
    assertThatCode(() -> new User(
        new OAuthInfo(OAuthProvider.KAKAO, "123", new Email("test123@gmail.com"), "image")))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("유저 생성 시 입력받은 값이 null일 경우 에외를 던진다.")
  void user_NullPointerExceptionTest() {
    // when, then
    assertThatThrownBy(() -> new User(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("유저를 등록할 수 있다.")
  void registerUserTest() {
    // given
    User user = new User();

    // when, then
    user.registerUser(new Profile("nickname"));
  }

  @Test
  @DisplayName("유저 등록 시 입력받은 값이 null이라면 예외를 던진다.")
  void registerUser_NullPointerExceptionTest() {
    // given
    User user = new User();

    // when, then
    assertThatThrownBy(() -> user.registerUser(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("프로필 사진을 변경할 수 있다.")
  void updateProfileImageUrlTest() {
    // given
    String beforeImage = "beforeImage";
    String afterImage = "afterImage";
    User user = new User(
        new OAuthInfo(OAuthProvider.KAKAO, "123", new Email("test123@gmail.com"), beforeImage));

    // when
    user.updateProfileImageUrl(afterImage);

    // then
    assertThat(user.getOauthInfo().getProfileImageUrl()).isEqualTo(afterImage);
  }

  @Test
  @DisplayName("프로필 사진 변경 시 입력받은 값이 null일 경우 예외를 던진다.")
  void updateProfileImageUrl_NullPointerExceptionTest() {
    // given
    String beforeImage = "beforeImage";
    User user = new User(
        new OAuthInfo(OAuthProvider.KAKAO, "123", new Email("test123@gmail.com"), beforeImage));

    // when, then
    assertThatThrownBy(() -> user.updateProfileImageUrl(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("닉네임을 변경할 수 있다.")
  void updateNicknameTest() {
    // given
    String beforeNickname = "beforeNickname";
    String afterNickname = "afterNickname";
    User user = new User(
        new OAuthInfo(OAuthProvider.KAKAO, "123", new Email("test123@gmail.com"), "image"));
    user.registerUser(new Profile(beforeNickname));

    // when
    user.updateNickname(afterNickname);

    // then
    assertThat(user.getProfile().getNickname()).isEqualTo(afterNickname);
  }

  @Test
  @DisplayName("닉네임 변경 시 입력받은 값이 null일 경우 예외를 던진다.")
  void updateNickname_NullPointerExceptionTest() {
    // given
    User user = new User(
        new OAuthInfo(OAuthProvider.KAKAO, "123", new Email("test123@gmail.com"), "image"));

    // when, then
    assertThatThrownBy(() -> user.updateNickname(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("알림 정보를 변경할 수 있다.")
  void updateNotificationInfoTest() {
    // given
    User user = new User(
        new OAuthInfo(OAuthProvider.KAKAO, "123", new Email("test123@gmail.com"), "image"));
    NotificationInfo notificationInfo = new NotificationInfo();

    // when, then
    assertThatCode(() -> user.updateNotificationInfo(notificationInfo)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("알림 정보 변경 시 입력받은 값이 null일 경우 예외를 던진다.")
  void updateNotificationInfo_NullPointerExceptionTest() {
    // given
    User user = new User(
        new OAuthInfo(OAuthProvider.KAKAO, "123", new Email("test123@gmail.com"), "image"));

    // when, then
    assertThatThrownBy(() -> user.updateNotificationInfo(null)).isInstanceOf(
        NullPointerException.class);
  }

  @Test
  @DisplayName("매칭 완료 시 유저 상태가 MATCHING이 아니라면 예외를 던진다.")
  void completeMatching_BadRequestExceptionTest() {
    // given
    ChattingRoom chattingRoom = new ChattingRoom();
    User user = new User();
    user.setIdleStatus();

    // when, then
    assertThatThrownBy(() -> user.completeMatching(chattingRoom))
        .isInstanceOf(BadRequestException.class);
  }

  @Test
  @DisplayName("채팅방 입장 시 유저 상태가 CHATTING_UNCONNECTED이 아니라면 예외를 던진다.")
  void enterChattingRoom_BadRequestExceptionTest() {
    // given
    User user = new User();
    user.setIdleStatus();

    // when, then
    assertThatThrownBy(user::enterChattingRoom).isInstanceOf(BadRequestException.class);
  }

  @Test
  @DisplayName("채팅방 퇴장 시 유저 상태가 CHATTING_CONNECTED이 아니라면 예외를 던진다.")
  void exitChattingRoom_BadRequestExceptionTest() {
    // given
    User user = new User();
    user.setIdleStatus();

    // when, then
    assertThatThrownBy(user::exitChattingRoom).isInstanceOf(BadRequestException.class);
  }

  @Test
  @DisplayName("유저를 블랙리스트 처리한다.")
  void convertToBlacklistTest() {
    // given
    User user = new User();

    // when
    user.convertToBlacklist();

    // then
    assertTrue(user.isBlacklisted());
  }

}
