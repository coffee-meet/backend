package coffeemeet.server.chatting.history.implement;

import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.domain.repository.UserChattingHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class UserChattingHistoryCommand {

  private final UserChattingHistoryRepository userChattingHistoryRepository;

  public void createUserChattingHistory(List<UserChattingHistory> userChattingHistories) {
    userChattingHistoryRepository.saveAll(userChattingHistories);
  }

}
