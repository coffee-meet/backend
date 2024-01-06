package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.implement.ChattingMessageHistoryCommand;
import coffeemeet.server.chatting.history.implement.ChattingRoomHistoryCommand;
import coffeemeet.server.chatting.history.implement.UserChattingHistoryCommand;
import coffeemeet.server.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingMigrationProcessor {

  private static final int FIXED_MESSAGE_BATCH_SIZE = 1000;

  private final ChattingMessageQuery chattingMessageQuery;
  private final ChattingMessageCommand chattingMessageCommand;
  private final ChattingMessageHistoryCommand chattingMessageHistoryCommand;
  private final ChattingRoomCommand chattingRoomCommand;
  private final ChattingRoomHistoryCommand chattingRoomHistoryCommand;
  private final UserChattingHistoryCommand userChattingHistoryCommand;


  public ChattingRoomHistory backUpChattingRoom(ChattingRoom chattingRoom,
      List<User> chattingRoomUsers) {
    ChattingRoomHistory chattingRoomHistory = chattingRoomHistoryCommand.createChattingRoomHistory(
        chattingRoom);

    userChattingHistoryCommand.createUserChattingHistory(
        chattingRoomUsers.stream().map(user -> new UserChattingHistory(user, chattingRoomHistory))
            .toList());

    return chattingRoomHistory;
  }

  public void migrateChattingMessagesToHistoryInChattingRoom(final ChattingRoom chattingRoom,
      final ChattingRoomHistory chattingRoomHistory, final Long chattingRoomLastMessageId) {
    Long messageId = chattingRoomLastMessageId;
    while (true) {
      List<ChattingMessage> messages = chattingMessageQuery.getChattingMessagesLessThanOrEqualToMessageId(
          chattingRoom, messageId,
          FIXED_MESSAGE_BATCH_SIZE);

      chattingMessageHistoryCommand.createChattingMessageHistory(
          messages.stream()
              .map(
                  chattingMessage ->
                      new ChattingMessageHistory(
                          chattingMessage.getMessage(),
                          chattingRoomHistory,
                          chattingMessage.getCreatedAt(),
                          chattingMessage.getUser()
                      )
              ).toList());

      chattingMessageCommand.deleteChattingMessages(messages);
      if (messages.size() < FIXED_MESSAGE_BATCH_SIZE) {
        break;
      }

      int lastMessageIndex = messages.size() - 1;
      messageId = messages.get(lastMessageIndex).getId();
    }
  }

  public void deleteChattingRoom(ChattingRoom chattingRoom, List<User> chattingRoomUsers) {
    chattingRoomUsers.forEach(user -> {
      user.setIdleStatus();
      user.deleteChattingRoom();
    });
    chattingRoomCommand.deleteChattingRoom(chattingRoom);
  }

}
