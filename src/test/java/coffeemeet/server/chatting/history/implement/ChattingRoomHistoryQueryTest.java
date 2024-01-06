package coffeemeet.server.chatting.history.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.repository.ChattingRoomHistoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingRoomHistoryQueryTest {

  @InjectMocks
  private ChattingRoomHistoryQuery chattingRoomHistoryQuery;

  @Mock
  private ChattingRoomHistoryRepository chattingRoomHistoryRepository;

  @Test
  @DisplayName("채팅방 기록 아이디로 해당 채팅방을 가져올 수 있다.")
  void getChattingRoomHistoryBy() {
    // given
    Long chattingRoomHistoryId = 1L;
    ChattingRoomHistory chattingRoomHistory = new ChattingRoomHistory(1L, "멋쟁이토마토");

    given(chattingRoomHistoryRepository.findById(anyLong())).willReturn(
        Optional.of(chattingRoomHistory));

    // when
    ChattingRoomHistory foundChattingRoomHistory = chattingRoomHistoryQuery.getChattingRoomHistoryBy(
        chattingRoomHistoryId);

    // then
    assertThat(foundChattingRoomHistory.getId()).isEqualTo(chattingRoomHistoryId);
  }

}
