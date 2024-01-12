package coffeemeet.server.chatting.history.implement;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.repository.ChattingRoomHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingRoomHistoryCommand {

  private final ChattingRoomHistoryRepository chattingRoomHistoryRepository;

  public ChattingRoomHistory createChattingRoomHistory(ChattingRoom chattingRoom) {
    return chattingRoomHistoryRepository.saveAndFlush(
        new ChattingRoomHistory(chattingRoom.getId(), chattingRoom.getName()));
  }

}
