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

  public List<ChattingMessage> getChattingMessagesLessThanMessageId(ChattingRoom chattingRoom,
      Long messageId,
      int pageSize) {
    return chattingMessageQueryRepository.findChattingMessagesLessThanCursorId(chattingRoom,
        messageId,
        pageSize);
  }

  public List<ChattingMessage> getChattingMessagesLessThanOrEqualToMessageId(
      ChattingRoom chattingRoom,
      Long messageId,
      int pageSize) {
    return chattingMessageQueryRepository.findChattingMessagesLessThanOrEqualToCursorId(
        chattingRoom,
        messageId, pageSize);
  }

}
