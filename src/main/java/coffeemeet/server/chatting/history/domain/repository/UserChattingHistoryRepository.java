package coffeemeet.server.chatting.history.domain.repository;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChattingHistoryRepository extends JpaRepository<UserChattingHistory, Long> {

  boolean existsByUserId(Long userId);

  List<UserChattingHistory> findAllByChattingRoomHistory(ChattingRoomHistory chattingRoomHistory);

  List<UserChattingHistory> findAllByUser(User user);

}
