package coffeemeet.server.chatting.current.implement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingMessageQueryRepository;
import coffeemeet.server.chatting.current.domain.repository.ChattingMessageRepository;
import coffeemeet.server.common.fixture.ChattingFixture;
import coffeemeet.server.common.fixture.UserFixture;
import coffeemeet.server.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingMessageCommandTest {

  @InjectMocks
  private ChattingMessageCommand chattingMessageCommand;

  @Mock
  private ChattingMessageRepository chattingMessageRepository;

  @Mock
  private ChattingMessageQueryRepository chattingMessageQueryRepository;

  @DisplayName("새로운 채팅 메세지를 저장할 수 있다.")
  @Test
  void createChattingMessageTest() {
    // given
    String content = "content";
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    User user = UserFixture.user();

    // when
    chattingMessageCommand.createChattingMessage(content, chattingRoom, user);

    // then
    then(chattingMessageRepository).should(only()).save(any(ChattingMessage.class));
  }

  @DisplayName("채팅방의 메세지를 전체 삭제할 수 있다.")
  @Test
  void deleteAllChattingMessagesByTest() {
    // given
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(10);

    // when
    chattingMessageCommand.deleteChattingMessages(chattingMessages);

    // then
    then(chattingMessageRepository).should(only()).deleteAllInBatch(chattingMessages);
  }

}
