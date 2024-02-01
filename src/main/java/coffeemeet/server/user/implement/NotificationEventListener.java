package coffeemeet.server.user.implement;

import coffeemeet.server.admin.domain.UserNotificationEvent;
import coffeemeet.server.common.infrastructure.FCMNotificationSender;
import coffeemeet.server.user.domain.NotificationInfo;
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

}
