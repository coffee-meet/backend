package coffeemeet.server.chatting.current.service;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageQuery;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingRoomStatusDto;
import coffeemeet.server.chatting.current.service.dto.Chatting;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.implement.ChattingMessageHistoryCommand;
import coffeemeet.server.chatting.history.implement.ChattingRoomHistoryCommand;
import coffeemeet.server.chatting.history.implement.UserChattingHistoryCommand;
import coffeemeet.server.common.infrastructure.FCMNotificationSender;
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

  private static final String CHATTING_END_MESSAGE = "채팅이 종료되었습니다!";

  private final ChattingRoomCommand chattingRoomCommand;
  private final ChattingRoomQuery chattingRoomQuery;
  private final ChattingMessageQuery chattingMessageQuery;
  private final ChattingRoomHistoryCommand chattingRoomHistoryCommand;
  private final ChattingMessageHistoryCommand chattingMessageHistoryCommand;
  private final UserChattingHistoryCommand userChattingHistoryCommand;
  private final UserQuery userQuery;
  private final FCMNotificationSender fcmNotificationSender;

  public Long createChattingRoom() {
    return chattingRoomCommand.createChattingRoom().getId();
  }

  @Transactional
  public ChattingListDto searchMessages(Long roomId, Long firstMessageId, int pageSize) {
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(roomId);
    List<ChattingMessage> chattingMessages = chattingMessageQuery.findMessages(chattingRoom,
        firstMessageId,
        pageSize);
    boolean hasNext = chattingMessages.size() >= pageSize;
    List<Chatting> chattingList = chattingMessages.stream()
        .map(message -> Chatting.of(message.getUser(), message)).toList();
    return ChattingListDto.of(chattingList, hasNext);
  }

  @Transactional
  public void deleteChattingRoom(Long roomId) {
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(roomId);
    List<User> users = userQuery.getUsersByRoom(chattingRoom);
    List<ChattingMessage> allMessages = chattingMessageQuery.findAllMessages(chattingRoom);
    backUpChattingRoom(allMessages, users, chattingRoom);
    chattingRoomCommand.removeChattingRoom(chattingRoom);
    sendChattingEndAlarm(users);
    updateUserStatusToIdle(users);
  }

  private void sendChattingEndAlarm(List<User> users) {
    Set<NotificationInfo> notificationInfos = users.stream().map(User::getNotificationInfo)
        .collect(Collectors.toSet());
    fcmNotificationSender.sendMultiNotifications(notificationInfos, CHATTING_END_MESSAGE);
  }

  private void backUpChattingRoom(List<ChattingMessage> allMessages, List<User> users,
      ChattingRoom chattingRoom) {
    ChattingRoomHistory chattingRoomHistory = chattingRoomHistoryCommand.createChattingRoomHistory(
        chattingRoom);
    chattingMessageHistoryCommand.createChattingMessageHistory(allMessages.stream().map(
            chattingMessage -> new ChattingMessageHistory(chattingMessage.getMessage(),
                chattingRoomHistory, chattingMessage.getCreatedAt(), chattingMessage.getUser()))
        .toList());
    userChattingHistoryCommand.createUserChattingHistory(
        users.stream().map(user -> new UserChattingHistory(user, chattingRoomHistory)).toList());
  }

  private void updateUserStatusToIdle(List<User> users) {
    users.forEach(User::setIdleStatus);
  }

  public ChattingRoomStatusDto checkChattingRoomStatus(Long roomId) {
    return ChattingRoomStatusDto.from(chattingRoomQuery.existsBy(roomId));
  }
}
