package coffeemeet.server.chatting.history.infrastructure;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingMessageHistoryRepository extends
    JpaRepository<ChattingMessageHistory, Long>, ChattingMessageHistoryBulkRepository {

}
