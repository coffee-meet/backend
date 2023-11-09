package coffeemeet.server.chatting.current.service;

import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingRoomServiceTest {

  @InjectMocks
  private ChattingRoomService chattingRoomService;

  @Mock
  private ChattingRoomCommand chattingRoomCommand;

  @DisplayName("채팅방을 만들 수 있다.")
  @Test
  void createChattingRoomTest() {
    // given
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    given(chattingRoomCommand.saveChattingRoom()).willReturn(chattingRoom);

    // when
    Long roomId = chattingRoomService.createChattingRoom();

    // then
    Assertions.assertThat(roomId).isEqualTo(chattingRoom.getId());
  }

}
