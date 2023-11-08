package coffeemeet.server.chatting.current.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto.Response;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.user.domain.User;
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

  @DisplayName("채팅 메세지를 만들고, 클라이언트에 채팅 응답을 반환할 수 있다.")
  @Test
  void chattingTest() {
    // given
    User user = UserFixture.user();
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    String content = "내용";
    ChattingMessage chattingMessage = ChattingFixture.chattingMessage(content);

    given(userQuery.getUserById(anyLong())).willReturn(user);
    given(chattingRoomQuery.getChattingRoomById(anyLong())).willReturn(chattingRoom);
    given(chattingMessageCommand.saveChattingMessage(content, chattingRoom, user)).willReturn(
        chattingMessage);

    // when
    Response response = chattingMessageService.chatting(chattingRoom.getId(), content,
        user.getId());

    // then
    assertThat(response.content()).isEqualTo(content);
  }

}
