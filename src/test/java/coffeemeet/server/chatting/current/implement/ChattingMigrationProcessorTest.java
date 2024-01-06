package coffeemeet.server.chatting.current.implement;

import static coffeemeet.server.common.fixture.ChattingFixture.chattingMessages;
import static coffeemeet.server.common.fixture.ChattingFixture.chattingRoom;
import static coffeemeet.server.common.fixture.ChattingFixture.chattingRoomHistory;
import static coffeemeet.server.common.fixture.UserFixture.fourUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.implement.ChattingMessageHistoryCommand;
import coffeemeet.server.chatting.history.implement.ChattingRoomHistoryCommand;
import coffeemeet.server.chatting.history.implement.UserChattingHistoryCommand;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.domain.UserStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingMigrationProcessorTest {

  @InjectMocks
  private ChattingMigrationProcessor chattingMigrationProcessor;
  @Mock
  private ChattingMessageQuery chattingMessageQuery;
  @Mock
  private ChattingMessageCommand chattingMessageCommand;
  @Mock
  private ChattingMessageHistoryCommand chattingMessageHistoryCommand;
  @Mock
  private ChattingRoomCommand chattingRoomCommand;
  @Mock
  private ChattingRoomHistoryCommand chattingRoomHistoryCommand;
  @Mock
  private UserChattingHistoryCommand userChattingHistoryCommand;

  @Test
  @DisplayName("채팅방 히스토리와 사용자 히스토리가 생성하여 채팅방 백업을 할 수 있다.")
  void backUpChattingRoomTest() {
    // given
    ChattingRoom chattingRoom = chattingRoom();
    List<User> chattingRoomUsers = fourUsers();

    given(chattingRoomHistoryCommand.createChattingRoomHistory(chattingRoom)).willReturn(
        chattingRoomHistory());

    // when
    chattingMigrationProcessor.backUpChattingRoom(chattingRoom, chattingRoomUsers);

    // then
    then(userChattingHistoryCommand).should().createUserChattingHistory(any());
  }

  @Test
  @DisplayName("채팅 메시지를 히스토리로 마이그레이션할 수 있다.")
  void migrateChattingMessagesToHistoryInChattingRoomTest() {
    // given
    ChattingRoom chattingRoom = chattingRoom();
    ChattingRoomHistory chattingRoomHistory = chattingRoomHistory();
    Long firstMessageId = 1L;

    List<ChattingMessage> chattingMessages = chattingMessages(3);

    given(chattingMessageQuery.getChattingMessagesLessThanOrEqualToMessageId(chattingRoom,
        firstMessageId, 1000))
        .willReturn(chattingMessages);

    // when
    chattingMigrationProcessor.migrateChattingMessagesToHistoryInChattingRoom(chattingRoom,
        chattingRoomHistory, firstMessageId);

    // then
    then(chattingMessageHistoryCommand).should().createChattingMessageHistory(any());
    then(chattingMessageCommand).should().deleteChattingMessages(any());
  }

  @Test
  @DisplayName("사용자와 채팅방 객체 참조를 끊고 사용자 상태가 변경하고 채팅방을 삭제할 수 있다.")
  void deleteChattingRoomTest() {
    // given
    ChattingRoom chattingRoom = chattingRoom();
    List<User> chattingRoomUsers = fourUsers();

    // when
    chattingMigrationProcessor.deleteChattingRoom(chattingRoom, chattingRoomUsers);

    // then
    then(chattingRoomCommand).should().deleteChattingRoom(chattingRoom);
    assertThat(chattingRoomUsers)
        .allMatch(
            user -> user.getUserStatus() == UserStatus.IDLE && user.getChattingRoom() == null);
  }

}
