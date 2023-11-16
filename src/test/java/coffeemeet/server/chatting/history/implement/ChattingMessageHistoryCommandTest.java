package coffeemeet.server.chatting.history.implement;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.infrastructure.ChattingMessageHistoryRepository;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingMessageHistoryCommandTest {

  @InjectMocks
  private ChattingMessageHistoryCommand chattingMessageHistoryCommand;

  @Mock
  private ChattingMessageHistoryRepository chattingMessageHistoryRepository;

  @DisplayName("채팅 메세지 내역을 만들 수 있다.")
  @Test
  void createChattingMessageHistoryTest() {
    // given
    int size = 10;
    List<ChattingMessageHistory> chattingMessageHistories = ChattingFixture.chattingMessageHistories(
        size);

    // when
    chattingMessageHistoryCommand.createChattingMessageHistory(chattingMessageHistories);

    // then
    then(chattingMessageHistoryRepository).should(only()).saveAll(anyList());
  }

}
