package coffeemeet.server.chatting.current.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingMessageQueryRepository;
import coffeemeet.server.common.fixture.ChattingFixture;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingMessageQueryTest {

  @InjectMocks
  private ChattingMessageQuery chattingMessageQuery;

  @Mock
  private ChattingMessageQueryRepository chattingMessageQueryRepository;

  @DisplayName("Cursor id보다 작은 채팅 메세지를 페이지 조회할 수 있다.")
  @Test
  void findMessagesTest() {
    // given
    int pageSize = 50;
    Long messageId = 51L;

    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(pageSize);
    given(
        chattingMessageQueryRepository.findChattingMessagesLessThanMessageId(chattingRoom, messageId,
            pageSize)).willReturn(chattingMessages);

    // when
    List<ChattingMessage> messages = chattingMessageQuery.getChattingMessagesLessThanMessageId(
        chattingRoom, messageId, pageSize);

    // then
    assertThat(messages).isEqualTo(chattingMessages);
  }

  @DisplayName("Cursor id와 같거나 작은 채팅 메세지를 페이지 조회할 수 있다.")
  @Test
  void findAllMessagesTest() {
    // given
    int pageSize = 50;
    Long messageId = 50L;
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(pageSize);
    given(chattingMessageQueryRepository.findChattingMessagesLessThanOrEqualToMessageId(chattingRoom,
        messageId, pageSize)).willReturn(chattingMessages);

    // when
    List<ChattingMessage> allMessages = chattingMessageQuery.getChattingMessagesLessThanOrEqualToMessageId(
        chattingRoom, messageId, pageSize);

    // then
    Assertions.assertThat(allMessages).isEqualTo(chattingMessages);
  }

}
