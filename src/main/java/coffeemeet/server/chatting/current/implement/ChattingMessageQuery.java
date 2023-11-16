package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingMessageQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChattingMessageQuery {

  private final ChattingMessageQueryRepository chattingMessageQueryRepository;

  public List<ChattingMessage> findMessages(ChattingRoom chattingRoom, Long firstMessageId,
      int pageSize) {
    return chattingMessageQueryRepository.findChattingMessages(chattingRoom, firstMessageId,
        pageSize);
  }

  public List<ChattingMessage> findAllMessages(ChattingRoom chattingRoom) {
    return chattingMessageQueryRepository.findAllChattingMessagesByChattingRoom(chattingRoom);
  }

}
