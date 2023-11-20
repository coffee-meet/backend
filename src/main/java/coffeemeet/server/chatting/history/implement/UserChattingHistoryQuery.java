package coffeemeet.server.chatting.history.implement;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.infrastructure.UserChattingHistoryRepository;
import coffeemeet.server.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserChattingHistoryQuery {

  private final UserChattingHistoryRepository userChattingHistoryRepository;

  public List<UserChattingHistory> getUserChattingHistoriesBy(
      ChattingRoomHistory chattingRoomHistory) {
    return userChattingHistoryRepository.findAllByChattingRoomHistory(chattingRoomHistory);
  }

  public List<UserChattingHistory> getUserChattingHistoriesBy(User user) {
    return userChattingHistoryRepository.findAllByUser(user);
  }

}
