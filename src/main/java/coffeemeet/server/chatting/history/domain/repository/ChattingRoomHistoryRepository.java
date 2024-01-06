package coffeemeet.server.chatting.history.domain.repository;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomHistoryRepository extends JpaRepository<ChattingRoomHistory, Long> {

  Set<ChattingRoomHistory> findByIdIn(Collection<Long> ids);

}
