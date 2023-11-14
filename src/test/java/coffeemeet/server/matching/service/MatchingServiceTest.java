package coffeemeet.server.matching.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.matching.implement.MatchingQueueQuery;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

  @InjectMocks
  private MatchingService matchingService;

  @Mock
  private ChattingRoomCommand chattingRoomCommand;

  @Mock
  private MatchingQueueCommand matchingQueueCommand;

  @Mock
  private UserCommand userCommand;

  @Mock
  private UserQuery userQuery;

  @Mock
  private CertificationQuery certificationQuery;

  @Mock
  private MatchingQueueQuery matchingQueueQuery;

  @Mock
  private FCMNotificationSender fcmNotificationSender;

  @DisplayName("매칭을 시작할 수 있다.")
  @Test
  void startTest() {
    // given
    String companyName = "회사명";
    long userId = 1;
    long matchingQueueSizeByCompany = 4;
    ChattingRoom chattingRoom = new ChattingRoom();
    Set<Long> userIds = new HashSet<>(Set.of(1L, 2L, 3L, 4L));

    NotificationInfo notificationInfo = UserFixture.notificationInfo();
    NotificationInfo notificationInfo1 = UserFixture.notificationInfo();
    NotificationInfo notificationInfo2 = UserFixture.notificationInfo();
    Set<NotificationInfo> notificationInfos = new HashSet<>(
        Set.of(notificationInfo, notificationInfo1, notificationInfo2));

    given(certificationQuery.getCompanyNameByUserId(anyLong())).willReturn(companyName);
    willDoNothing().given(matchingQueueCommand).enqueueUserByCompanyName(any(), anyLong());
    given(matchingQueueQuery.sizeByCompany(any())).willReturn(matchingQueueSizeByCompany);
    given(matchingQueueQuery.dequeueMatchingGroupSize(any(), anyLong())).willReturn(userIds);
    given(chattingRoomCommand.createChattingRoom()).willReturn(chattingRoom);
    willDoNothing().given(userCommand).assignUsersToChattingRoom(anySet(), any());
    willDoNothing().given(fcmNotificationSender)
        .sendMultiNotificationsWithData(anySet(), any(), any(), any());
    given(userQuery.getNotificationInfosByIdSet(anySet())).willReturn(notificationInfos);

    // when, then
    assertThatCode(() -> matchingService.start(userId))
        .doesNotThrowAnyException();
  }

}
