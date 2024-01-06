package coffeemeet.server.chatting.current.domain.repository;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {

}
