package coffeemeet.server.chatting.history.implement;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.infrastructure.ChattingRoomHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingRoomHistoryCommand {

  private final ChattingRoomHistoryRepository chattingRoomHistoryRepository;

  public ChattingRoomHistory createChattingRoomHistory() {
    return chattingRoomHistoryRepository.save(new ChattingRoomHistory());
  }

}
