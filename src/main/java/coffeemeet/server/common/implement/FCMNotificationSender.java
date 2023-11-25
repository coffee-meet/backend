package coffeemeet.server.common.implement;

import static coffeemeet.server.common.execption.GlobalErrorCode.INVALID_FCM_TOKEN;
import static coffeemeet.server.common.execption.GlobalErrorCode.PUSH_NOTIFICATION_SEND_FAILURE;
import static com.google.firebase.messaging.MessagingErrorCode.INVALID_ARGUMENT;
import static com.google.firebase.messaging.MessagingErrorCode.UNREGISTERED;

import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.common.execption.NotificationFailException;
import coffeemeet.server.user.domain.NotificationInfo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FCMNotificationSender {

  private static final String INVALID_FCM_TOKEN_MESSAGE = "사용된 토큰(%s)이 유효하지 않습니다.";
  private static final String PUSH_NOTIFICATION_SEND_FAILURE_MESSAGE = "푸시 알림 전송에 실패했습니다. 토큰: %s";
  private final FirebaseMessaging firebaseMessaging;

  public void sendNotification(NotificationInfo notificationInfo, String content) {
    if (!notificationInfo.isSubscribedToNotification()) {
      return;
    }

    Notification notification = createNotification(content);
    Message message = Message.builder().setToken(notificationInfo.getToken())
        .setNotification(notification)
        .build();

    try {
      firebaseMessaging.send(message);
    } catch (FirebaseMessagingException e) {
      handleFirebaseMessagingException(e, notificationInfo.getToken());
    }
  }

  public void sendMultiNotifications(Set<NotificationInfo> notificationInfos, String content) {
    notificationInfos.removeIf(notificationInfo -> !notificationInfo.isSubscribedToNotification());

    Set<String> tokens = notificationInfos.stream().map(
        NotificationInfo::getToken
    ).collect(Collectors.toUnmodifiableSet());

    Notification notification = createNotification(content);
    MulticastMessage message = MulticastMessage.builder().addAllTokens(tokens)
        .setNotification(notification).build();

    try {
      firebaseMessaging.sendMulticast(message);
    } catch (FirebaseMessagingException e) {
      handleFirebaseMessagingException(e, String.join(", ", tokens)); //
    }
  }

  public void sendMultiNotificationsWithData(Set<NotificationInfo> notificationInfos,
      String content, String key, String value) {
    notificationInfos.removeIf(notificationInfo -> !notificationInfo.isSubscribedToNotification());

    Set<String> tokens = notificationInfos.stream().map(
        NotificationInfo::getToken
    ).collect(Collectors.toUnmodifiableSet());

    Notification notification = createNotification(content);
    MulticastMessage message = MulticastMessage.builder().addAllTokens(tokens)
        .putData(key, value)
        .setNotification(notification).build();

    try {
      firebaseMessaging.sendMulticast(message);
    } catch (FirebaseMessagingException e) {
      handleFirebaseMessagingException(e, String.join(", ", tokens));
    }
  }

  private void handleFirebaseMessagingException(FirebaseMessagingException e, String token) {
    MessagingErrorCode messagingErrorCode = e.getMessagingErrorCode();

    if (messagingErrorCode == UNREGISTERED || messagingErrorCode == INVALID_ARGUMENT) {
      throw new InvalidInputException(INVALID_FCM_TOKEN,
          String.format(INVALID_FCM_TOKEN_MESSAGE, token));
    }
    throw new NotificationFailException(PUSH_NOTIFICATION_SEND_FAILURE,
        String.format(PUSH_NOTIFICATION_SEND_FAILURE_MESSAGE, token));
  }

  private Notification createNotification(String content) {
    return Notification.builder().setTitle("커피밋").setBody(content).build();
  }

}
