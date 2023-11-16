package coffeemeet.server.chatting.current.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingMessageQueryRepository;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
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

  @DisplayName("채팅 메세지를 조회할 수 있다.")
  @Test
  void findMessagesTest() {
    // given
    int pageSize = 50;
    Long firstMessageId = 51L;

    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(chattingRoom, 50);
    given(chattingMessageQueryRepository.findChattingMessages(chattingRoom, firstMessageId,
        pageSize)).willReturn(chattingMessages);

    // when
    List<ChattingMessage> messages = chattingMessageQuery.findMessages(chattingRoom, firstMessageId,
        pageSize);

    // then
    assertThat(messages).isEqualTo(chattingMessages);
  }

  @DisplayName("전체 채팅 메세지를 조회할 수 있다.")
  @Test
  void findAllMessagesTest() {
    // given
    int size = 10;
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(chattingRoom, size);

    // when
    given(chattingMessageQueryRepository.findAllChattingMessages(chattingRoom)).willReturn(
        chattingMessages);

    // then
    List<ChattingMessage> allMessages = chattingMessageQuery.findAllMessages(chattingRoom);
    Assertions.assertThat(allMessages).isEqualTo(chattingMessages);
  }

}
