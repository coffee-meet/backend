package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.implement.ChattingMessageHistoryCommand;
import coffeemeet.server.chatting.history.implement.ChattingRoomHistoryCommand;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingRoomMigrationProcessor {

  private static final int FIXED_MESSAGE_BATCH_SIZE = 1000;

  private final ChattingMessageQuery chattingMessageQuery;
  private final ChattingMessageCommand chattingMessageCommand;
  private final ChattingMessageHistoryCommand chattingMessageHistoryCommand;
  private final ChattingRoomCommand chattingRoomCommand;
  private final ChattingRoomQuery chattingRoomQuery;
  private final UserQuery userQuery;
  private final ChattingRoomHistoryCommand chattingRoomHistoryCommand;

  @Async
  @Transactional
  public void migrate(Long roomId, Long chattingRoomLastMessageId) {
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(roomId);
    List<User> chattingRoomUsers = userQuery.getUsersByRoom(chattingRoom);
    ChattingRoomHistory chattingRoomHistory = chattingRoomHistoryCommand.createChattingRoomHistory(
        chattingRoom, chattingRoomUsers);
    migrateChattingMessages(chattingRoom, chattingRoomHistory,
        chattingRoomLastMessageId);
    chattingRoomCommand.deleteChattingRoom(chattingRoom);
  }

  private void migrateChattingMessages(final ChattingRoom chattingRoom,
      final ChattingRoomHistory chattingRoomHistory, final Long chattingRoomLastMessageId) {
    Long messageId = chattingRoomLastMessageId;
    while (true) {
      List<ChattingMessage> messages = chattingMessageQuery.getChattingMessagesLessThanOrEqualToMessageId(
          chattingRoom, messageId,
          FIXED_MESSAGE_BATCH_SIZE);

      chattingMessageHistoryCommand.createChattingMessageHistory(
          convertCurrentToHistory(chattingRoomHistory, messages)
      );

      chattingMessageCommand.deleteChattingMessages(messages);
      if (messages.size() < FIXED_MESSAGE_BATCH_SIZE) {
        break;
      }

      int lastMessageIndex = messages.size() - 1;
      messageId = messages.get(lastMessageIndex).getId();
    }
  }

  private List<ChattingMessageHistory> convertCurrentToHistory(
      ChattingRoomHistory chattingRoomHistory, List<ChattingMessage> messages
  ) {
    return messages.stream()
        .map(
            chattingMessage ->
                new ChattingMessageHistory(
                    chattingMessage.getMessage(),
                    chattingRoomHistory,
                    chattingMessage.getCreatedAt(),
                    chattingMessage.getUser()
                )
        ).toList();
  }

}
