package coffeemeet.server.chatting.current.service;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import coffeemeet.server.chatting.exception.ChattingErrorCode;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingMessageService {

  private static final String SOCKET_SESSION_NOT_FOUND_MESSAGE = "소켓 연결 정보가 없습니다.";

  private final Map<String, Long> sessions = new ConcurrentHashMap<>();
  private final ChattingMessageCommand chattingMessageCommand;
  private final ChattingRoomQuery chattingRoomQuery;
  private final UserQuery userQuery;
  private final FCMNotificationSender fcmNotificationSender;

  public ChattingDto.Response chatting(String sessionId, Long roomId, String content) {
    Long userId = getUserId(sessionId);
    ChattingRoom room = chattingRoomQuery.getChattingRoomById(roomId);
    List<User> users = userQuery.getUsersByRoom(room);
//    Set<NotificationInfo> unConnectedUserNotificationInfos = getUnConnectedUserNotificationInfos(
//        users);
//    fcmNotificationSender.sendMultiNotifications(unConnectedUserNotificationInfos, content);
    User user = userQuery.getUserById(userId);
    ChattingMessage chattingMessage = chattingMessageCommand.saveChattingMessage(content,
        room, user);
    return ChattingDto.Response.of(user, chattingMessage);
  }

  private Set<NotificationInfo> getUnConnectedUserNotificationInfos(List<User> users) {
    return users.stream()
        .filter(user -> user.getUserStatus() == UserStatus.CHATTING_UNCONNECTED)
        .map(User::getNotificationInfo)
        .collect(Collectors.toSet());
  }

  private Long getUserId(String sessionId) {
    Long userId = sessions.get(sessionId);
    if (userId == null) {
      throw new NotFoundException(
          ChattingErrorCode.SOCKET_SESSION_NOT_FOUND,
          SOCKET_SESSION_NOT_FOUND_MESSAGE
      );
    }
    return userId;
  }

  public void storeSocketSession(String sessionId, String userId) {
    User user = userQuery.getUserById(Long.valueOf(userId));
    user.enterChattingRoom();
    sessions.put(sessionId, Long.valueOf(userId));
  }

  public void expireSocketSession(String sessionId) {
    Long userId = sessions.get(sessionId);
    User user = userQuery.getUserById(userId);
    user.exitChattingRoom();
    sessions.remove(sessionId);
  }

}
