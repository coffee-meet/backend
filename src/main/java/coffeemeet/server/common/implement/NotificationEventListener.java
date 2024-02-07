package coffeemeet.server.common.implement;

import coffeemeet.server.common.domain.UserNotificationEvent;
import coffeemeet.server.common.domain.UsersNotificationEvent;
import coffeemeet.server.common.infrastructure.FCMNotificationSender;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.implement.UserQuery;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final FCMNotificationSender fcmNotificationSender;
  private final UserQuery userQuery;

  @Async
  @TransactionalEventListener
  public void send(UserNotificationEvent event) {
    NotificationInfo notificationInfo = userQuery.getNotificationInfoByUserId(event.userId());
    fcmNotificationSender.sendNotification(notificationInfo, event.message());
  }

  @Async
  @TransactionalEventListener
  public void send(UsersNotificationEvent event) {
    Set<NotificationInfo> notificationInfos = userQuery.getNotificationInfosByIdSet(
        event.userIds());
    fcmNotificationSender.sendMultiNotifications(notificationInfos, event.message());
  }

}
