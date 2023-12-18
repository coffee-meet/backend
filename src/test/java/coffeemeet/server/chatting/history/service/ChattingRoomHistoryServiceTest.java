package coffeemeet.server.chatting.history.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.implement.ChattingMessageHistoryQuery;
import coffeemeet.server.chatting.history.implement.ChattingRoomHistoryQuery;
import coffeemeet.server.chatting.history.implement.UserChattingHistoryQuery;
import coffeemeet.server.chatting.history.service.dto.ChattingMessageHistoryListDto;
import coffeemeet.server.common.fixture.ChattingFixture;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingRoomHistoryServiceTest {

  @InjectMocks
  private ChattingRoomHistoryService chattingRoomHistoryService;

  @Mock
  private UserChattingHistoryQuery userChattingHistoryQuery;

  @Mock
  private ChattingRoomHistoryQuery chattingRoomHistoryQuery;

  @Mock
  private ChattingMessageHistoryQuery chattingMessageHistoryQuery;

  @Mock
  private UserQuery userQuery;

  // TODO: 11/20/23 로직 수정 후 테스트 작성
  @DisplayName("이전 채팅방 전체 내역을 조회할 수 있다.")
  @Test
  void searchChattingRoomHistoriesTest() {
    // given

    // when

    // then
  }

  @DisplayName("이전 채팅방 메세지 내역을 조회할 수 있다.")
  @ParameterizedTest
  @CsvSource(value = {"51, 50"})
  void searchChattingMessageHistoriesTest(Long firstMessageId, int pageSize) {
    // given
    ChattingRoomHistory chattingRoomHistory = ChattingFixture.chattingRoomHistory();
    List<ChattingMessageHistory> chattingMessageHistories = ChattingFixture.chattingMessageHistories(
        pageSize);
    Long roomHistoryId = chattingRoomHistory.getId();

    given(chattingRoomHistoryQuery.getChattingRoomHistoryBy(roomHistoryId)).willReturn(
        chattingRoomHistory);
    given(chattingMessageHistoryQuery.getMessageHistories(chattingRoomHistory, firstMessageId,
        pageSize)).willReturn(
        chattingMessageHistories);

    // when
    ChattingMessageHistoryListDto responses = chattingRoomHistoryService.searchChattingMessageHistories(
        roomHistoryId, firstMessageId,
        pageSize);

    // then
    assertThat(responses.contents()).hasSize(pageSize);
  }

}
