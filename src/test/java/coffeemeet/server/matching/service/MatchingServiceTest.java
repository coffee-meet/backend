package coffeemeet.server.matching.service;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.certificatedCertifications;
import static coffeemeet.server.common.fixture.entity.ChattingFixture.chattingRoom;
import static coffeemeet.server.common.fixture.entity.UserFixture.fourUsers;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.matching.implement.MatchingQueueQuery;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    List<User> users = fourUsers();
    Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
    String companyName = "회사명";
    List<Certification> certifications = certificatedCertifications(users, companyName);

    User requestedUser = users.get(0);
    Certification requestedUsersCertification = certifications.get(0);
    ChattingRoom chattingRoom = chattingRoom();
    Set<NotificationInfo> notificationInfos = users.stream().map(User::getNotificationInfo).collect(
        Collectors.toSet());

    given(certificationQuery.getCertificationByUserId(requestedUser.getId())).willReturn(
        requestedUsersCertification);
    given(matchingQueueQuery.sizeByCompany(companyName)).willReturn(Long.valueOf(users.size()));
    given(chattingRoomCommand.createChattingRoom()).willReturn(chattingRoom);
    given(matchingQueueQuery.dequeueMatchingGroupSize(companyName, users.size())).willReturn(
        fourUsers().stream().map(User::getId).collect(Collectors.toSet()));
    given(chattingRoomCommand.createChattingRoom()).willReturn(chattingRoom);
    given(userQuery.getNotificationInfosByIdSet(userIds)).willReturn(
        notificationInfos);

    // when
    matchingService.startMatching(requestedUser.getId());

    // then
    then(matchingQueueCommand).should()
        .enqueueUserByCompanyName(companyName, requestedUser.getId());
    then(userCommand).should().setToMatching(requestedUser.getId());
    then(userCommand).should().assignUsersToChattingRoom(userIds, chattingRoom);
    then(fcmNotificationSender).should()
        .sendMultiNotificationsWithData(notificationInfos, "두근두근 커피밋 채팅을 시작하세요!",
            "chattingRoomId", String.valueOf(chattingRoom.getId()));
  }

  @Test
  @DisplayName("매칭을 취소할 수 있다.")
  void cancelMatching() {
    // given
    Long userId = 1L;
    String companyName = "회사명";

    given(certificationQuery.getCompanyNameByUserId(userId)).willReturn(companyName);
    willDoNothing().given(matchingQueueCommand).deleteUserByUserId(companyName, userId);

    // when
    matchingService.cancelMatching(userId);

    // then
    then(userCommand).should(only()).setToIdle(userId);
  }

}
