package coffeemeet.server.common.implement;

import static coffeemeet.server.common.fixture.UserFixture.content;
import static coffeemeet.server.common.fixture.UserFixture.notificationInfo;
import static coffeemeet.server.common.fixture.UserFixture.notificationInfos;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.common.infrastructure.FCMNotificationSender;
import coffeemeet.server.user.domain.NotificationInfo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FCMNotificationSenderTest {

  @InjectMocks
  private FCMNotificationSender fcmNotificationSender;

  @Mock
  private FirebaseMessaging firebaseMessaging;

  @Test
  void sendNotificationByTokenTest() throws FirebaseMessagingException {
    // given
    NotificationInfo notificationInfo = notificationInfo();
    String content = content();

    given(firebaseMessaging.send(any())).willReturn(any());

    // when
    fcmNotificationSender.sendNotification(notificationInfo, content);

    // then
    then(firebaseMessaging).should(only()).send(any());
  }

  @Test
  void sendNotificationByTokens() throws FirebaseMessagingException {
    // given
    Set<NotificationInfo> notificationInfos = notificationInfos();
    String content = content();

    given(firebaseMessaging.sendMulticast(any())).willReturn(any());

    // when
    fcmNotificationSender.sendMultiNotifications(notificationInfos, content);

    // then
    then(firebaseMessaging).should(only()).sendMulticast(any());
  }

}
