package coffeemeet.server.chatting.current.infrastructure;

import static coffeemeet.server.chatting.current.domain.QChattingMessage.chattingMessage;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
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
public class ChattingMessageQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public List<ChattingMessage> findChattingMessagesLessThanMessageId(ChattingRoom chattingRoom,
      Long cursorId, int pageSize) {
    List<ChattingMessage> messages = jpaQueryFactory
        .selectFrom(chattingMessage)
        .where(chattingMessage.chattingRoom.eq(chattingRoom)
            .and(ltChattingMessageId(cursorId)))
        .orderBy(chattingMessage.id.desc())
        .limit(pageSize)
        .fetch();
    messages.sort(Comparator.comparingLong(
        ChattingMessage::getId)); // TODO: 2024/01/03 이거 왜 아이디로 정렬하는건가요? created 순으로 정렬하는게 맞지 않나요?
    return messages;
  }

  public List<ChattingMessage> findChattingMessagesLessThanOrEqualToMessageId(
      ChattingRoom chattingRoom,
      Long cursorId, int pageSize) {
    return jpaQueryFactory
        .selectFrom(chattingMessage)
        .where(chattingMessage.chattingRoom.eq(chattingRoom)
            .and(loeChattingMessageId(cursorId)))
        .orderBy(chattingMessage.id.desc())
        .limit(pageSize)
        .fetch();
  }

  private BooleanExpression ltChattingMessageId(Long chattingMessageId) {
    if (chattingMessageId == null || chattingMessageId == 0L) {
      return null;
    }
    return chattingMessage.id.lt(chattingMessageId);
  }

  private BooleanExpression loeChattingMessageId(Long chattingMessageId) {
    if (chattingMessageId == null || chattingMessageId == 0L) {
      return null;
    }
    return chattingMessage.id.loe(chattingMessageId);
  }

}
