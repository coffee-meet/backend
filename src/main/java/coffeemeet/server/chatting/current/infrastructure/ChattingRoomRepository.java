package coffeemeet.server.chatting.current.infrastructure;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

  boolean existsById(Long chattingRoomId);

  Set<ChattingRoom> findByIdIn(Collection<Long> ids);

}
