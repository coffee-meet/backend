package coffeemeet.server.chatting.current.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageQuery;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto.Response;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.user.domain.User;
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
    User user = UserFixture.user();
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(chattingRoom, user,
        pageSize);
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

}
