package coffeemeet.server.chatting.history.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.repository.ChattingMessageHistoryRepository;
import coffeemeet.server.chatting.history.domain.repository.ChattingRoomHistoryRepository;
import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.common.fixture.ChattingFixture;
import coffeemeet.server.common.fixture.UserFixture;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class ChattingMessageHistoryQueryRepositoryTest extends RepositoryTestConfig {

  @Autowired
  private ChattingMessageHistoryRepository chattingMessageHistoryRepository;

  @Autowired
  private ChattingMessageHistoryQueryRepository chattingMessageHistoryQueryRepository;

  @Autowired
  private ChattingRoomHistoryRepository chattingRoomHistoryRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void setUp() {
    this.entityManager
        .createNativeQuery("ALTER TABLE chatting_message_histories ALTER COLUMN id RESTART WITH 1")
        .executeUpdate();
  }

  @AfterEach
  void tearDown() {
    chattingMessageHistoryRepository.deleteAll();
    chattingRoomHistoryRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("firstMessageId 보다 1 작은 메세지 내역부터 페이지 사이즈만큼 조회한다.")
  @ParameterizedTest
  @CsvSource(value = {"51, 50"})
  void findChattingMessageHistoriesTest(Long firstMessageId, int pageSize) {
    // given
    int fixtureSize = 110;

    ChattingRoomHistory chattingRoomHistory = chattingRoomHistoryRepository.save(
        ChattingFixture.chattingRoomHistory());
    User user = userRepository.save(UserFixture.user());
    List<ChattingMessageHistory> chattingMessageHistories = ChattingFixture.chattingMessageHistories(
        chattingRoomHistory, user,
        fixtureSize);
    List<ChattingMessageHistory> chattingMessageHistories1 = chattingMessageHistoryRepository.saveAll(
        chattingMessageHistories);
    for (ChattingMessageHistory chattingMessageHistory : chattingMessageHistories1) {
      System.out.println(chattingMessageHistory.getId());
    }
    // when
    List<ChattingMessageHistory> responses = chattingMessageHistoryQueryRepository.findChattingMessageHistories(
        chattingRoomHistory,
        firstMessageId, pageSize);

    // then
    int lastIndex = responses.size() - 1;
    assertAll(
        () -> assertThat(responses).hasSize(pageSize),
        () -> assertThat(responses.get(lastIndex).getId()).isEqualTo(firstMessageId - 1)
    );
  }

}
