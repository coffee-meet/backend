package coffeemeet.server.chatting.current.implement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingMessageRepository;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.user.domain.User;
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

  @DisplayName("새로운 채팅 메세지를 저장할 수 있다.")
  @Test
  void saveChattingMessageTest() {
    // given
    String content = "content";
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    User user = UserFixture.user();

    // when
    chattingMessageCommand.saveChattingMessage(content, chattingRoom, user);

    // then
    then(chattingMessageRepository).should(only()).save(any(ChattingMessage.class));
  }

}
