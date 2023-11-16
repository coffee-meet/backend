package coffeemeet.server.chatting.history.implement;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.infrastructure.UserChattingHistoryRepository;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserChattingHistoryCommandTest {

  @InjectMocks
  private UserChattingHistoryCommand userChattingHistoryCommand;

  @Mock
  private UserChattingHistoryRepository userChattingHistoryRepository;

  @DisplayName("유저 채팅 내역을 생성할 수 있다.")
  @Test
  void createUserChattingHistoryTest() {
    // given
    int size = 10;
    List<UserChattingHistory> userChattingHistories = ChattingFixture.userChattingHistories(size);

    // when
    userChattingHistoryCommand.createUserChattingHistory(userChattingHistories);

    // then
    then(userChattingHistoryRepository).should(only()).saveAll(anyList());
  }

}
