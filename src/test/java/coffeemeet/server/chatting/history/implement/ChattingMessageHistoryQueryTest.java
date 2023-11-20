package coffeemeet.server.chatting.history.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.infrastructure.ChattingMessageHistoryQueryRepository;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingMessageHistoryQueryTest {

  @InjectMocks
  private ChattingMessageHistoryQuery chattingMessageHistoryQuery;

  @Mock
  private ChattingMessageHistoryQueryRepository chattingMessageHistoryQueryRepository;

  @DisplayName("채팅 메세지 내역을 조회할 수 있다.")
  @Test
  void findMessageHistoriesTest() {
    // given
    int pageSize = 50;
    Long firstMessageId = 51L;

    ChattingRoomHistory chattingRoomHistory = ChattingFixture.chattingRoomHistory();
    List<ChattingMessageHistory> chattingMessageHistories = ChattingFixture.chattingMessageHistories(
        50);
    given(chattingMessageHistoryQueryRepository.findChattingMessageHistories(chattingRoomHistory,
        firstMessageId,
        pageSize)).willReturn(chattingMessageHistories);

    // when
    List<ChattingMessageHistory> messages = chattingMessageHistoryQuery.getMessageHistories(
        chattingRoomHistory, firstMessageId,
        pageSize);

    // then
    assertThat(messages).isEqualTo(chattingMessageHistories);
  }

}
