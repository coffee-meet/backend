package coffeemeet.server.chatting.history.infrastructure;

import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChattingHistoryRepository extends JpaRepository<UserChattingHistory, Long> {

  boolean existsByUserId(Long userId);
}
