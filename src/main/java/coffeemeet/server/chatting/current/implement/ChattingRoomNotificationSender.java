package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.common.infrastructure.FCMNotificationSender;
import coffeemeet.server.user.domain.NotificationInfo;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChattingRoomNotificationSender {

  private static final String CHATTING_END_MESSAGE = "채팅이 종료되었습니다!";

  private final FCMNotificationSender fcmNotificationSender;

  public void notifyChattingRoomEnd(Set<NotificationInfo> notificationInfos) {
    fcmNotificationSender.sendMultiNotifications(notificationInfos, CHATTING_END_MESSAGE);
  }

}
