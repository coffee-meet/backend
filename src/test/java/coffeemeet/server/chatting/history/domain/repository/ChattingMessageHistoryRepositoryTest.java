package coffeemeet.server.chatting.history.domain.repository;

import static coffeemeet.server.common.fixture.ChattingFixture.chattingMessageHistories;
import static coffeemeet.server.common.fixture.ChattingFixture.chattingRoomHistory;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ChattingMessageHistoryRepositoryTest extends RepositoryTestConfig {

  @Autowired
  private ChattingMessageHistoryRepository chattingMessageHistoryRepository;
  @Autowired
  private ChattingRoomHistoryRepository chattingRoomHistoryRepository;
  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("주어진 객체 모두 저장할 수 있다.")
  void saveAllInBatchTest() {
    // given
    ChattingRoomHistory chattingRoomHistory = chattingRoomHistory();
    User user = user();
    int size = 10;

    chattingRoomHistoryRepository.save(chattingRoomHistory);
    userRepository.save(user);

    List<ChattingMessageHistory> chattingMessageHistories = chattingMessageHistories(
        chattingRoomHistory, user, size);

    // when
    chattingMessageHistoryRepository.saveAllInBatch(chattingMessageHistories);

    // then
    List<ChattingMessageHistory> all = chattingMessageHistoryRepository.findAll();
    assertThat(all).extracting("message")
        .isEqualTo(
            chattingMessageHistories.stream()
                .map(ChattingMessageHistory::getMessage)
                .toList()
        );
  }

}
