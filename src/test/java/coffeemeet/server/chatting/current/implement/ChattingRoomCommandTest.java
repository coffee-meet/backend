package coffeemeet.server.chatting.current.implement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingRoomRepository;
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

  @DisplayName("채팅방을 생성할 수 있다.")
  @Test
  void saveChattingRoomTest() {
    // given, when
    chattingRoomCommand.createChattingRoom();

    // then
    then(chattingRoomRepository).should(only()).save(any(ChattingRoom.class));
  }

}
