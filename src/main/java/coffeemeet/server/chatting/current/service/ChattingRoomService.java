package coffeemeet.server.chatting.current.service;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageQuery;
import coffeemeet.server.chatting.current.implement.ChattingMigrationProcessor;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomNotificationSender;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.implement.ChattingRoomUserValidator;
import coffeemeet.server.chatting.current.service.dto.Chatting;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
import coffeemeet.server.chatting.current.service.dto.ChattingRoomStatusDto;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChattingRoomService {

  private final ChattingRoomCommand chattingRoomCommand;
  private final ChattingRoomQuery chattingRoomQuery;
  private final ChattingMessageQuery chattingMessageQuery;
  private final ChattingMigrationProcessor chattingMigrationProcessor;
  private final ChattingRoomNotificationSender chattingRoomNotificationSender;
  private final UserQuery userQuery;
  private final ChattingRoomUserValidator chattingRoomUserValidator;

  public Long createChattingRoom() {
    return chattingRoomCommand.createChattingRoom().getId();
  }

  @Transactional
  public ChattingListDto searchMessages(Long requestedUserId, Long roomId, Long lastMessageId,
      int pageSize) {
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(roomId);
    List<User> chattingRoomUsers = userQuery.getUsersByRoom(chattingRoom);
    chattingRoomUserValidator.validateUserInChattingRoom(requestedUserId, chattingRoomUsers);

    List<ChattingMessage> chattingMessages = chattingMessageQuery.getChattingMessagesLessThanMessageId(
        chattingRoom, lastMessageId, pageSize);
    boolean hasNext = chattingMessages.size() >= pageSize;

    List<Chatting> chattingList = chattingMessages.stream()
        .map(message -> Chatting.of(message.getUser(), message)).toList();
    return ChattingListDto.of(chattingList, hasNext);
  }

  @Transactional
  public void exitChattingRoom(Long requestUserId, Long roomId, Long chattingRoomLastMessageId) {
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(roomId);
    List<User> chattingRoomUsers = userQuery.getUsersByRoom(chattingRoom);
    chattingRoomUserValidator.validateUserInChattingRoom(requestUserId, chattingRoomUsers);

    ChattingRoomHistory chattingRoomHistory = chattingMigrationProcessor.backUpChattingRoom(
        chattingRoom, chattingRoomUsers);
    chattingMigrationProcessor.migrateChattingMessagesToHistoryInChattingRoom(chattingRoom,
        chattingRoomHistory,
        chattingRoomLastMessageId);
    chattingMigrationProcessor.deleteChattingRoom(chattingRoom, chattingRoomUsers);

    Set<NotificationInfo> notificationInfos = chattingRoomUsers.stream()
        .map(User::getNotificationInfo)
        .collect(Collectors.toSet());
    chattingRoomNotificationSender.notifyChattingRoomEnd(notificationInfos);
  }

  public ChattingRoomStatusDto checkChattingRoomStatus(Long roomId) {
    return ChattingRoomStatusDto.from(chattingRoomQuery.existsBy(roomId));
  }

}
