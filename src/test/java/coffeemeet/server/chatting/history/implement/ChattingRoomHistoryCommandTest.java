//package coffeemeet.server.chatting.history.implement;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.BDDMockito.any;
//import static org.mockito.BDDMockito.given;
//
//import coffeemeet.server.chatting.current.domain.ChattingRoom;
//import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
//import coffeemeet.server.chatting.history.domain.repository.ChattingRoomHistoryRepository;
//import coffeemeet.server.common.fixture.ChattingFixture;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class ChattingRoomHistoryCommandTest {
//
//  @InjectMocks
//  private ChattingRoomHistoryCommand chattingRoomHistoryCommand;
//
//  @Mock
//  private ChattingRoomHistoryRepository chattingRoomHistoryRepository;
//
//  @DisplayName("채팅방 내역을 만들 수 있다.")
//  @Test
//  void createChattingRoomHistoryTest() {
//    // given
//    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
//    ChattingRoomHistory chattingRoomHistory = ChattingFixture.chattingRoomHistory();
//    given(chattingRoomHistoryRepository.saveAndFlush(any())).willReturn(chattingRoomHistory);
//
//    // when
//    ChattingRoomHistory savedChattingRoomHistory = chattingRoomHistoryCommand.createChattingRoomHistory(
//        chattingRoom);
//
//    // then
//    assertThat(savedChattingRoomHistory).isEqualTo(chattingRoomHistory);
//  }
//
//}
