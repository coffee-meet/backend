package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingMessageNotificationEvenet;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.domain.ChattingRoomNotificationEvent;
import coffeemeet.server.common.infrastructure.FCMNotificationSender;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.implement.UserQuery;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ChattingRoomEventListener {

  private final FCMNotificationSender fcmNotificationSender;
  private final UserQuery userQuery;
  private final ChattingRoomQuery chattingRoomQuery;

  @Async
  @TransactionalEventListener
  public void send(ChattingRoomNotificationEvent event) {
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(event.chattingRoomId());
    List<User> usersInRoom = userQuery.getUsersByRoom(chattingRoom);

    Set<NotificationInfo> notificationInfos = usersInRoom.stream()
        .map(User::getNotificationInfo)
        .collect(Collectors.toSet());
    fcmNotificationSender.sendMultiNotifications(notificationInfos, event.message());
  }

  @Async
  @TransactionalEventListener
  public void send(ChattingMessageNotificationEvenet event) {
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(event.roomId());
    List<User> users = userQuery.getUsersByRoom(chattingRoom);
    Set<NotificationInfo> unConnectedUserNotificationInfos = users.stream()
        .filter(user -> user.getUserStatus() == UserStatus.CHATTING_UNCONNECTED)
        .map(User::getNotificationInfo)
        .collect(Collectors.toSet());
    fcmNotificationSender.sendMultiNotifications(
        unConnectedUserNotificationInfos,
        MessageFormat.format("{0}: {1}", event.senderNickName(), event.message())
    );
  }

}
