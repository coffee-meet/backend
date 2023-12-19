package coffeemeet.server.chatting.current.service;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.implement.ChattingSessionCommand;
import coffeemeet.server.chatting.current.implement.ChattingSessionQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import coffeemeet.server.common.infrastructure.FCMNotificationSender;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingMessageService {

  private final ChattingSessionQuery chattingSessionQuery;
  private final ChattingSessionCommand chattingSessionCommand;
  private final ChattingMessageCommand chattingMessageCommand;
  private final ChattingRoomQuery chattingRoomQuery;
  private final UserQuery userQuery;
  private final UserCommand userCommand;
  private final FCMNotificationSender fcmNotificationSender;

  public ChattingDto chat(String sessionId, Long roomId, String content) {
    Long userId = chattingSessionQuery.getUserIdById(sessionId);
    ChattingRoom room = chattingRoomQuery.getChattingRoomById(roomId);
    List<User> users = userQuery.getUsersByRoom(room);
    User user = userQuery.getUserById(userId);
    sendChattingAlarm(user.getProfile().getNickname(), content, users);
    ChattingMessage chattingMessage = chattingMessageCommand.createChattingMessage(content,
        room, user);
    return ChattingDto.of(user, chattingMessage);
  }

  private void sendChattingAlarm(String chattingUserNickname, String content, List<User> users) {
    Set<NotificationInfo> unConnectedUserNotificationInfos = getUnConnectedUserNotificationInfos(
        users);
    fcmNotificationSender.sendMultiNotifications(unConnectedUserNotificationInfos,
        chattingUserNickname + " : " + content);
  }

  private Set<NotificationInfo> getUnConnectedUserNotificationInfos(List<User> users) {
    return users.stream()
        .filter(user -> user.getUserStatus() == UserStatus.CHATTING_UNCONNECTED)
        .map(User::getNotificationInfo)
        .collect(Collectors.toSet());
  }

  public void storeSocketSession(String sessionId, String userId) {
    userCommand.enterToChattingRoom(Long.valueOf(userId));
    chattingSessionCommand.connect(sessionId, Long.parseLong(userId));
  }

  public void expireSocketSession(String sessionId) {
    Long userId = chattingSessionQuery.getUserIdById(sessionId);
    userCommand.exitChattingRoom(userId);
    chattingSessionCommand.disconnect(sessionId);
  }

}
