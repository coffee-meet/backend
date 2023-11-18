package coffeemeet.server.chatting.current.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingRoomRepository;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingRoomQueryTest {

  @InjectMocks
  private ChattingRoomQuery chattingRoomQuery;

  @Mock
  private ChattingRoomRepository chattingRoomRepository;

  @DisplayName("채팅방을 조회할 수 있다.")
  @Test
  void getChattingRoomByIdTest() {
    // given
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    Long chattingRoomId = chattingRoom.getId();
    given(chattingRoomRepository.findById(chattingRoomId)).willReturn(Optional.of(chattingRoom));

    // when
    ChattingRoom foundChattingRoom = chattingRoomQuery.getChattingRoomById(chattingRoomId);

    // then
    assertThat(foundChattingRoom).isEqualTo(chattingRoom);
  }

}
