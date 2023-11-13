package coffeemeet.server.chatting.current.service;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
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
    // given, when
    chattingRoomService.createChattingRoom();

    // then
    then(chattingRoomCommand).should(only()).createChattingRoom();
  }

}
