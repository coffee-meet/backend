package coffeemeet.server.chatting.history.infrastructure;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import java.util.List;

public interface ChattingMessageHistoryBulkRepository {

  void saveAllInBatch(List<ChattingMessageHistory> messages);

}
