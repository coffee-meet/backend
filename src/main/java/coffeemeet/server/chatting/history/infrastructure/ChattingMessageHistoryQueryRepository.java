package coffeemeet.server.chatting.history.infrastructure;

import static coffeemeet.server.chatting.history.domain.QChattingMessageHistory.chattingMessageHistory;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChattingMessageHistoryQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public List<ChattingMessageHistory> findChattingMessageHistories(
      ChattingRoomHistory chattingRoomHistory,
      Long firstMessageId, int pageSize) {
    List<ChattingMessageHistory> messages = jpaQueryFactory
        .selectFrom(chattingMessageHistory)
        .where(chattingMessageHistory.chattingRoomHistory.eq(chattingRoomHistory)
            .and(ltChattingMessageId(firstMessageId)))
        .orderBy(chattingMessageHistory.id.desc())
        .limit(pageSize)
        .fetch();
    messages.sort(Comparator.comparingLong(ChattingMessageHistory::getId));
    return messages;
  }

  private BooleanExpression ltChattingMessageId(Long chattingMessageHistoryId) {
    if (chattingMessageHistoryId == null || chattingMessageHistoryId == 0L) {
      return null;
    }
    return chattingMessageHistory.id.lt(chattingMessageHistoryId);
  }

}
