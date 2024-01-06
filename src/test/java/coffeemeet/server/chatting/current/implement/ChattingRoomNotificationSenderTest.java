package coffeemeet.server.chatting.current.implement;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import coffeemeet.server.common.fixture.UserFixture;
import coffeemeet.server.common.infrastructure.FCMNotificationSender;
import coffeemeet.server.user.domain.NotificationInfo;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingRoomNotificationSenderTest {

  @InjectMocks
  private ChattingRoomNotificationSender chattingRoomNotificationSender;

  @Mock
  private FCMNotificationSender fcmNotificationSender;

  @Test
  @DisplayName("채팅방 종료 알람을 보낼 수 있다.")
  void notifyChattingRoomEnd() {
    // given
    Set<NotificationInfo> notificationInfos = UserFixture.notificationInfos();

    // when
    chattingRoomNotificationSender.notifyChattingRoomEnd(notificationInfos);

    // then
    then(fcmNotificationSender).should().sendMultiNotifications(eq(notificationInfos), anyString());
  }

}
