package coffeemeet.server.chatting.history.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.infrastructure.UserChattingHistoryRepository;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserChattingHistoryQueryTest {

  @InjectMocks
  private UserChattingHistoryQuery userChattingHistoryQuery;

  @Mock
  private UserChattingHistoryRepository userChattingHistoryRepository;

  @DisplayName("채팅방 내역으로 유저 채팅 테이블을 조회할 수 있다.")
  @Test
  void getUserChattingHistoriesByChattingRoomHistoryTest() {
    // given
    int size = 10;

    ChattingRoomHistory chattingRoomHistory = ChattingFixture.chattingRoomHistory();
    List<UserChattingHistory> userChattingHistories = ChattingFixture.userChattingHistories(size);

    given(
        userChattingHistoryRepository.findAllByChattingRoomHistory(chattingRoomHistory)).willReturn(
        userChattingHistories);

    // when
    List<UserChattingHistory> response = userChattingHistoryQuery.getUserChattingHistoriesBy(
        chattingRoomHistory);

    // then
    assertThat(response).isEqualTo(userChattingHistories);
  }

  @DisplayName("유저로 유저 채팅 테이블을 조회할 수 있다.")
  @Test
  void getUserChattingHistoriesByUserTest() {
    // given
    int size = 10;

    User user = UserFixture.user();
    List<UserChattingHistory> userChattingHistories = ChattingFixture.userChattingHistories(size);

    given(userChattingHistoryRepository.findAllByUser(user)).willReturn(userChattingHistories);

    // when
    List<UserChattingHistory> response = userChattingHistoryQuery.getUserChattingHistoriesBy(
        user);

    // then
    assertThat(response).isEqualTo(userChattingHistories);
  }

}
