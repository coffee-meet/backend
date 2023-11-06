package coffeemeet.server.chatting.current.infrastructure;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

}
