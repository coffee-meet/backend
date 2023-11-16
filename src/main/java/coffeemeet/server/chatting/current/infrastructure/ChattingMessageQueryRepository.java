package coffeemeet.server.chatting.current.infrastructure;

import static coffeemeet.server.chatting.current.domain.QChattingMessage.chattingMessage;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChattingMessageQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public List<ChattingMessage> findChattingMessages(ChattingRoom chattingRoom,
      Long firstMessageId, int pageSize) {
    List<ChattingMessage> messages = jpaQueryFactory
        .selectFrom(chattingMessage)
        .where(chattingMessage.chattingRoom.eq(chattingRoom)
            .and(ltChattingMessageId(firstMessageId)))
        .orderBy(chattingMessage.id.desc())
        .limit(pageSize)
        .fetch();
    Collections.sort(messages, Comparator.comparingLong(m -> m.getId()));
    return messages;
  }

  public List<ChattingMessage> findAllChattingMessagesByChattingRoom(ChattingRoom chattingRoom) {
    return jpaQueryFactory
        .selectFrom(chattingMessage)
        .where(chattingMessage.chattingRoom.eq(chattingRoom))
        .fetch();
  }

  private BooleanExpression ltChattingMessageId(Long chattingMessageId) {
    if (chattingMessageId == null || chattingMessageId == 0L) {
      return null;
    }
    return chattingMessage.id.lt(chattingMessageId);
  }

}
