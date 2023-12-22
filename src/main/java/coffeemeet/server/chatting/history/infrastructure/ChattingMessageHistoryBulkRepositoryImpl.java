package coffeemeet.server.chatting.history.infrastructure;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChattingMessageHistoryBulkRepositoryImpl implements
    ChattingMessageHistoryBulkRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public void saveAllInBatch(List<ChattingMessageHistory> messages) {
    jdbcTemplate.batchUpdate("""
            INSERT INTO chatting_message_histories (message, created_at, chatting_room_history_id, user_id)
            VALUES (?, ?, ?, ?)
            """,
        messages,
        messages.size(),
        (ps, message) -> {
          ps.setString(1, message.getMessage());
          ps.setTimestamp(2, Timestamp.valueOf(message.getCreatedAt()));
          ps.setLong(3, message.getChattingRoomHistoryId());
          ps.setLong(4, message.getUserId());
        });
  }

}
