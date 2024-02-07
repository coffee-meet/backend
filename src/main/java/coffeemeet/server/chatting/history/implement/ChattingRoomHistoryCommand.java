package coffeemeet.server.chatting.history.implement;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.domain.repository.ChattingRoomHistoryRepository;
import coffeemeet.server.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingRoomHistoryCommand {

  private final ChattingRoomHistoryRepository chattingRoomHistoryRepository;
  private final UserChattingHistoryCommand userChattingHistoryCommand;

  public ChattingRoomHistory createChattingRoomHistory(ChattingRoom chattingRoom,
      List<User> chattingRoomUsers) {
    ChattingRoomHistory chattingRoomHistory = chattingRoomHistoryRepository.saveAndFlush(
        new ChattingRoomHistory(chattingRoom.getId(), chattingRoom.getName()));

    userChattingHistoryCommand.createUserChattingHistory(
        chattingRoomUsers.stream().map(user -> new UserChattingHistory(user, chattingRoomHistory))
            .toList());
    return chattingRoomHistory;
  }

}
