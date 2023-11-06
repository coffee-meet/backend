package coffeemeet.server.chatting.current.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.current.implement.ChattingMessageCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.user.implement.UserQuery;
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

  @DisplayName("채팅 메세지를 만들 수 있다.")
  @Test
  void createChattingMessageTest() {
    // given
    Long roomId = 1L;
    String content = "내용";
    Long userId = 1L;

    // when
    chattingMessageService.createChattingMessage(roomId, content, userId);

    // then
    then(userQuery).should(only()).getUserById(userId);
    then(chattingRoomQuery).should(only()).getChattingRoomById(roomId);
    then(chattingMessageCommand).should(only()).saveChattingMessage(any(), any(), any());
  }

}
