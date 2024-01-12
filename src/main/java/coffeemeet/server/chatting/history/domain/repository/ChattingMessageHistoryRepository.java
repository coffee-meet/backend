package coffeemeet.server.chatting.history.domain.repository;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingMessageHistoryRepository extends
    JpaRepository<ChattingMessageHistory, Long>, ChattingMessageHistoryJdbcRepository {

}
