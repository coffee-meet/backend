package coffeemeet.server.chatting.current.implement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingRoomRepository;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingRoomCommandTest {

  @InjectMocks
  private ChattingRoomCommand chattingRoomCommand;

  @Mock
  private ChattingRoomRepository chattingRoomRepository;

  @Mock
  private ChattingMessageCommand chattingMessageCommand;

  @Mock
  private UserQuery userQuery;

  @DisplayName("채팅방을 생성할 수 있다.")
  @Test
  void saveChattingRoomTest() {
    // given, when
    chattingRoomCommand.createChattingRoom();

    // then
    then(chattingRoomRepository).should(only()).save(any(ChattingRoom.class));
  }

  @DisplayName("채팅방을 삭제할 수 있다.")
  @Test
  void removeChattingRoomTest() {
    // given
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(10);
    List<User> users = UserFixture.fourUsers();
    given(userQuery.getUsersByRoom(chattingRoom)).willReturn(users);

    // when
    chattingRoomCommand.removeChattingRoom(chattingRoom);

    // then
    then(chattingMessageCommand).should(times(1)).deleteAllChattingMessagesBy(chattingRoom);
    then(chattingRoomRepository).should(only()).deleteById(chattingRoom.getId());
  }

}
