package coffeemeet.server.chatting.history.domain.repository;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import java.util.List;

public interface ChattingMessageHistoryJdbcRepository {

  void saveAllInBatch(List<ChattingMessageHistory> messages);

}
