package coffeemeet.server.chatting.history.implement;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.repository.ChattingMessageHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingMessageHistoryCommand {

  private final ChattingMessageHistoryRepository chattingMessageHistoryRepository;

  public void createChattingMessageHistory(List<ChattingMessageHistory> messages) {
    chattingMessageHistoryRepository.saveAllInBatch(messages);
  }

}
