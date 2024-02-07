package coffeemeet.server.chatting.current.service;

import static coffeemeet.server.common.fixture.ChattingFixture.chattingMessages;
import static coffeemeet.server.common.fixture.ChattingFixture.chattingRoom;
import static coffeemeet.server.common.fixture.UserFixture.chattingRoomUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageQuery;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomMigrationProcessor;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.implement.ChattingRoomUserValidator;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
import coffeemeet.server.chatting.current.service.dto.ChattingRoomStatusDto;
import coffeemeet.server.common.fixture.ChattingFixture;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
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
  private ChattingRoomMigrationProcessor chattingRoomMigrationProcessor;
  @Mock
  private UserQuery userQuery;
  @Mock
  private ChattingRoomUserValidator chattingRoomUserValidator;

  @DisplayName("채팅 메세지를 조회할 수 있다.")
  @ParameterizedTest
  @CsvSource(value = {"51, 50"})
  void searchMessagesTest(Long lastMessageId, int pageSize) {
    // given
    ChattingRoom chattingRoom = chattingRoom();
    Long chattingRoomId = chattingRoom.getId();
    List<ChattingMessage> chattingMessages = chattingMessages(pageSize);

    List<User> chattingRoomUsers = chattingRoomUsers();
    Long requestUserId = chattingRoomUsers.get(0).getId();
    chattingRoomUsers.forEach(user -> {
      user.exitChattingRoom();
      user.matching();
      user.completeMatching(chattingRoom);
    });

    given(chattingRoomQuery.getChattingRoomById(chattingRoomId)).willReturn(chattingRoom);
    given(userQuery.getUsersByRoom(chattingRoom)).willReturn(chattingRoomUsers);
    given(chattingMessageQuery.getChattingMessagesLessThanMessageId(chattingRoom, lastMessageId,
        pageSize)).willReturn(
        chattingMessages);

    // when
    ChattingListDto responses = chattingRoomService.searchMessages(requestUserId, chattingRoomId,
        lastMessageId,
        pageSize);

    // then
    assertThat(responses.contents()).hasSize(pageSize);
  }

  @DisplayName("현재 채팅방 백업 및 삭제 후, 유저의 상태 변경 및 알람 전송을 할 수 있다.")
  @Test
  void deleteChattingRoomTest() {
    // TODO: 2024/02/05 테스트 작성
  }

  @DisplayName("채팅방의 유무 상태를 조회할 수 있다.")
  @Test
  void checkChattingRoomStatusTest() {
    // given
    Long roomId = 1L;
    ChattingRoomStatusDto chattingRoomStatusDto = ChattingFixture.chatRoomStatusDto();

    given(chattingRoomQuery.existsBy(any())).willReturn(chattingRoomStatusDto.isExisted());

    // when
    ChattingRoomStatusDto response = chattingRoomService.checkChattingRoomStatus(roomId);

    // then
    assertThat(response).isEqualTo(chattingRoomStatusDto);
  }

}
