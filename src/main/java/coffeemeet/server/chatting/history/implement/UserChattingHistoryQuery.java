package coffeemeet.server.chatting.history.implement;

import coffeemeet.server.chatting.history.infrastructure.UserChattingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserChattingHistoryQuery {

  private final UserChattingHistoryRepository userChattingHistoryRepository;

  public boolean existsByUserId(Long userId) {
    return userChattingHistoryRepository.existsByUserId(userId);
  }

}
