package coffeemeet.server.chatting.history.implement;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.infrastructure.ChattingMessageHistoryQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChattingMessageHistoryQuery {

  private final ChattingMessageHistoryQueryRepository chattingMessageHistoryQueryRepository;

  public List<ChattingMessageHistory> getMessageHistories(ChattingRoomHistory chattingRoomHistory,
      Long firstMessageId,
      int pageSize) {
    return chattingMessageHistoryQueryRepository.findChattingMessageHistories(chattingRoomHistory,
        firstMessageId,
        pageSize);
  }

}
