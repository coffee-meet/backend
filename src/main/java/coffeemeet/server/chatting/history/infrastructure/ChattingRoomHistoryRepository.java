package coffeemeet.server.chatting.history.infrastructure;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomHistoryRepository extends JpaRepository<ChattingRoomHistory, Long> {

}
