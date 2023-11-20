package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingMessageQueryRepository;
import coffeemeet.server.chatting.current.infrastructure.ChattingMessageRepository;
import coffeemeet.server.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingMessageCommand {

  private final ChattingMessageRepository chattingMessageRepository;
  private final ChattingMessageQueryRepository chattingMessageQueryRepository;

  public ChattingMessage createChattingMessage(String content, ChattingRoom chattingRoom,
      User user) {
    return chattingMessageRepository.save(new ChattingMessage(content, chattingRoom, user));
  }

  public void deleteAllChattingMessagesBy(ChattingRoom chattingRoom) {
    List<ChattingMessage> messages = chattingMessageQueryRepository.findAllChattingMessagesByChattingRoom(
        chattingRoom);
    chattingMessageRepository.deleteAll(messages);
  }

}
