package coffeemeet.server.chatting.current.implement;

import static coffeemeet.server.common.fixture.ChattingFixture.chattingRoom;
import static coffeemeet.server.common.fixture.ChattingFixture.chattingRoomHistory;
import static coffeemeet.server.common.fixture.UserFixture.chattingRoomUsers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.implement.ChattingMessageHistoryCommand;
import coffeemeet.server.chatting.history.implement.ChattingRoomHistoryCommand;
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
class ChattingRoomMigrationProcessorTest {

  @InjectMocks
  private ChattingRoomMigrationProcessor chattingRoomMigrationProcessor;
  @Mock
  private ChattingMessageQuery chattingMessageQuery;
  @Mock
  private ChattingMessageCommand chattingMessageCommand;
  @Mock
  private ChattingMessageHistoryCommand chattingMessageHistoryCommand;
  @Mock
  private ChattingRoomCommand chattingRoomCommand;
  @Mock
  private ChattingRoomQuery chattingRoomQuery;
  @Mock
  private UserQuery userQuery;
  @Mock
  private ChattingRoomHistoryCommand chattingRoomHistoryCommand;

  @Test
  @DisplayName("채팅 내역(채팅방, 채팅메세지)를 History 테이블로 마이그레이션 할 수 있다.")
  void migrateTest() {
    // given
    Long chattingRoomLastMessageId = 1L;
    ChattingRoom chattingRoom = chattingRoom();
    Long roomId = chattingRoom.getId();
    ChattingRoomHistory chattingRoomHistory = chattingRoomHistory();
    List<User> users = chattingRoomUsers();

    given(chattingRoomQuery.getChattingRoomById(roomId)).willReturn(chattingRoom);
    given(userQuery.getUsersByRoom(chattingRoom)).willReturn(users);
    given(chattingRoomHistoryCommand.createChattingRoomHistory(chattingRoom, users)).willReturn(
        chattingRoomHistory);

    // when
    chattingRoomMigrationProcessor.migrate(roomId, chattingRoomLastMessageId);

    // then
    then(chattingMessageQuery).should(atLeastOnce()).getChattingMessagesLessThanOrEqualToMessageId(eq(chattingRoom), anyLong(), anyInt());
    then(chattingMessageHistoryCommand).should(atLeastOnce()).createChattingMessageHistory(any());
    then(chattingMessageCommand).should(atLeastOnce()).deleteChattingMessages(anyList());
    then(chattingRoomCommand).should(only()).deleteChattingRoom(chattingRoom);
  }

}
