package coffeemeet.server.chatting.current.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto.Response;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingMessageServiceTest {

  @InjectMocks
  private ChattingMessageService chattingMessageService;

  @Mock
  private ChattingMessageCommand chattingMessageCommand;

  @Mock
  private ChattingRoomQuery chattingRoomQuery;

  @Mock
  private UserQuery userQuery;

  @Mock
  private FCMNotificationSender fcmNotificationSender;

  @DisplayName("채팅 메세지를 만들고, 클라이언트에 채팅 응답을 반환할 수 있다.")
  @Test
  void chattingTest() {
    // given
    User user = UserFixture.user();
    List<User> users = UserFixture.users();
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    String content = "내용";
    ChattingMessage chattingMessage = ChattingFixture.chattingMessage(content);
    String sessionId = "sessionId";

    given(chattingRoomQuery.getChattingRoomById(chattingRoom.getId())).willReturn(chattingRoom);
    given(userQuery.getUserById(user.getId())).willReturn(user);
//    willDoNothing().given(fcmNotificationSender)
//        .sendMultiNotifications(anySet(), any());
    given(userQuery.getUsersByRoom(chattingRoom)).willReturn(users);
    given(chattingMessageCommand.saveChattingMessage(content, chattingRoom, user)).willReturn(
        chattingMessage);

    // when
    chattingMessageService.storeSocketSession(sessionId, String.valueOf(user.getId()));
    Response response = chattingMessageService.chatting(sessionId, chattingRoom.getId(), content);

    // then
    assertThat(response.content()).isEqualTo(content);
  }

  @DisplayName("세션을 저장하고, 유저의 상태를 연결된 상태로 바꿀수 있다.")
  @Test
  void storeSocketSessionTest() {
    // given
    String sessionId = "sessionId";
    User user = UserFixture.user();
    given(userQuery.getUserById(any())).willReturn(user);

    // when
    chattingMessageService.storeSocketSession(sessionId, String.valueOf(user.getId()));

    // then
    assertThat(user.getUserStatus()).isEqualTo(UserStatus.CHATTING_CONNECTED);
  }

  @DisplayName("세션을 만료하고, 유저의 상태를 연결되지 않은 상태로 바꿀수 있다.")
  @Test
  void expireSocketSessionTest() {
    // given
    String sessionId = "sessionId";
    User user = UserFixture.user();
    given(userQuery.getUserById(any())).willReturn(user);

    // when
    chattingMessageService.expireSocketSession(sessionId);

    // then
    assertThat(user.getUserStatus()).isEqualTo(UserStatus.CHATTING_UNCONNECTED);

  }

}
