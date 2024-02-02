package coffeemeet.server.chatting.current.service;

import static coffeemeet.server.common.fixture.ChattingFixture.chattingMessage;
import static coffeemeet.server.common.fixture.UserFixture.chattingRoomUsers;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.implement.ChattingSessionCommand;
import coffeemeet.server.chatting.current.implement.ChattingSessionQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import coffeemeet.server.common.infrastructure.FCMNotificationSender;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserCommand;
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
  private ChattingSessionCommand chattingSessionCommand;

  @Mock
  private ChattingSessionQuery chattingSessionQuery;

  @Mock
  private ChattingMessageCommand chattingMessageCommand;

  @Mock
  private ChattingRoomQuery chattingRoomQuery;

  @Mock
  private UserQuery userQuery;

  @Mock
  private UserCommand userCommand;

  @Mock
  private FCMNotificationSender fcmNotificationSender;

  @DisplayName("채팅 메세지를 만들고, 클라이언트에 채팅 응답을 반환할 수 있다.")
  @Test
  void chattingTest() {
    // given
    User user = user();
    List<User> users = chattingRoomUsers();
    ChattingRoom chattingRoom = users.get(0).getChattingRoom();
    Long roomId = chattingRoom.getId();
    String sessionId = "sessionId";
    String content = "메세지내용";
    ChattingMessage chattingMessage = chattingMessage(content);
    ChattingDto expected = ChattingDto.of(user, chattingMessage);

    given(chattingSessionQuery.getUserById(sessionId)).willReturn(user);
    given(chattingRoomQuery.getChattingRoomById(roomId)).willReturn(chattingRoom);
    given(chattingMessageCommand.createChattingMessage(content, chattingRoom, user)).willReturn(
        chattingMessage);

    // when
    ChattingDto chat = chattingMessageService.chat(sessionId, roomId, content);

    // then
    assertThat(chat)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @DisplayName("세션을 저장하고, 유저의 상태를 연결된 상태로 바꿀수 있다.")
  @Test
  void storeSocketSessionTest() {
    // given
    String sessionId = "sessionId";
    User user = user();
    Long userId = user.getId();

    // when
    chattingMessageService.storeSocketSession(sessionId, String.valueOf(userId));

    // then
    then(userCommand).should(only()).enterToChattingRoom(userId);
    then(chattingSessionCommand).should(only()).connect(sessionId, userId);
  }

  @DisplayName("세션을 만료하고, 유저의 상태를 연결되지 않은 상태로 바꿀수 있다.")
  @Test
  void expireSocketSessionTest() {
    // given
    String sessionId = "sessionId";
    User user = user();
    Long userId = user.getId();

    given(chattingSessionQuery.getUserIdById(sessionId)).willReturn(userId);

    // when
    chattingMessageService.expireSocketSession(sessionId);

    // then
    then(userCommand).should(only()).exitChattingRoom(userId);
    then(chattingSessionCommand).should(only()).disconnect(sessionId);
  }

}
