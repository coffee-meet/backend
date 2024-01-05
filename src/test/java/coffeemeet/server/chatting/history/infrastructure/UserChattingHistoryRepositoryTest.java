package coffeemeet.server.chatting.history.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.repository.ChattingRoomHistoryRepository;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.domain.repository.UserChattingHistoryRepository;
import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.common.fixture.ChattingFixture;
import coffeemeet.server.common.fixture.UserFixture;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserChattingHistoryRepositoryTest extends RepositoryTestConfig {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChattingRoomHistoryRepository chattingRoomHistoryRepository;

  @Autowired
  private UserChattingHistoryRepository userChattingHistoryRepository;

  @AfterEach
  void tearDown() {
    userChattingHistoryRepository.deleteAll();
    userRepository.deleteAll();
    chattingRoomHistoryRepository.deleteAll();
  }

  @DisplayName("채팅방 내역으로 유저 채팅 내역 테이블을 전체 조회할 수 있다.")
  @Test
  void findAllByChattingRoomHistoryTest() {
    // given
    int expectedSize = 1;

    User user = UserFixture.user();
    userRepository.save(user);

    ChattingRoomHistory chattingRoomHistory = ChattingFixture.chattingRoomHistory();
    chattingRoomHistoryRepository.save(chattingRoomHistory);

    UserChattingHistory userChattingHistory = ChattingFixture.userChattingHistory(user,
        chattingRoomHistory);
    userChattingHistoryRepository.save(userChattingHistory);

    // when
    List<UserChattingHistory> userChattingHistories = userChattingHistoryRepository.findAllByChattingRoomHistory(
        chattingRoomHistory);

    // then
    assertThat(userChattingHistories).hasSize(expectedSize);
  }

  @DisplayName("유저로 유저 채팅 내역 테이블을 전체 조회할 수 있다.")
  @Test
  void findAllByUserTest() {
    // given
    int expectedSize = 1;

    User user = UserFixture.user();
    userRepository.save(user);

    ChattingRoomHistory chattingRoomHistory = ChattingFixture.chattingRoomHistory();
    chattingRoomHistoryRepository.save(chattingRoomHistory);

    UserChattingHistory userChattingHistory = ChattingFixture.userChattingHistory(user,
        chattingRoomHistory);
    userChattingHistoryRepository.save(userChattingHistory);

    // when
    List<UserChattingHistory> userChattingHistories = userChattingHistoryRepository.findAllByUser(
        user);

    // then
    assertThat(userChattingHistories).hasSize(expectedSize);
  }

}
