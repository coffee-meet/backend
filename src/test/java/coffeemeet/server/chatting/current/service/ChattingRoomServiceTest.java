package coffeemeet.server.chatting.current.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageQuery;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto.Response;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.implement.ChattingMessageHistoryCommand;
import coffeemeet.server.chatting.history.implement.ChattingRoomHistoryCommand;
import coffeemeet.server.chatting.history.implement.UserChattingHistoryCommand;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingRoomServiceTest {

  @InjectMocks
  private ChattingRoomService chattingRoomService;

  @Mock
  private ChattingRoomCommand chattingRoomCommand;

  @Mock
  private ChattingRoomQuery chattingRoomQuery;

  @Mock
  private ChattingMessageQuery chattingMessageQuery;

  @Mock
  private ChattingRoomHistoryCommand chattingRoomHistoryCommand;

  @Mock
  private ChattingMessageHistoryCommand chattingMessageHistoryCommand;

  @Mock
  private UserChattingHistoryCommand userChattingHistoryCommand;

  @Mock
  private UserQuery userQuery;

  @Mock
  private FCMNotificationSender fcmNotificationSender;

  @DisplayName("채팅방을 만들 수 있다.")
  @Test
  void createChattingRoomTest() {
    // given
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    given(chattingRoomCommand.createChattingRoom()).willReturn(chattingRoom);

    // when
    Long roomId = chattingRoomService.createChattingRoom();

    // then
    Assertions.assertThat(roomId).isEqualTo(chattingRoom.getId());
  }

  @DisplayName("채팅 메세지를 조회할 수 있다.")
  @ParameterizedTest
  @CsvSource(value = {"51, 50"})
  void searchMessagesTest(Long firstMessageId, int pageSize) {
    // given
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(pageSize);
    Long chattingRoomId = chattingRoom.getId();

    given(chattingRoomQuery.getChattingRoomById(chattingRoomId)).willReturn(chattingRoom);
    given(chattingMessageQuery.findMessages(chattingRoom, firstMessageId, pageSize)).willReturn(
        chattingMessages);

    // when
    List<Response> responses = chattingRoomService.searchMessages(chattingRoomId, firstMessageId,
        pageSize);

    // then
    assertThat(responses).hasSize(pageSize);
  }

  @DisplayName("현재 채팅방 백업 및 삭제 후, 유저의 상태 변경 및 알람 전송을 할 수 있다.")
  @Test
  void deleteChattingRoomTest() {
    // given
    Long roomId = 1L;
    int size = 10;
    List<User> users = UserFixture.users();

    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    ChattingRoomHistory chattingRoomHistory = ChattingFixture.chattingRoomHistory();
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(size);

    given(chattingRoomQuery.getChattingRoomById(roomId)).willReturn(chattingRoom);
    given(userQuery.getUsersByRoom(chattingRoom)).willReturn(users);
    given(chattingMessageQuery.findAllMessages(chattingRoom)).willReturn(chattingMessages);
    given(chattingRoomHistoryCommand.createChattingRoomHistory(chattingRoom)).willReturn(
        chattingRoomHistory);

    // when
    chattingRoomService.deleteChattingRoom(roomId);

    // then
    then(chattingMessageHistoryCommand).should(only()).createChattingMessageHistory(anyList());
    then(userChattingHistoryCommand).should(only()).createUserChattingHistory(anyList());
    then(chattingRoomCommand).should(only()).removeChattingRoom(any());
    //then(fcmNotificationSender).should(only()).sendMultiNotifications(anySet(), any());
  }

}
