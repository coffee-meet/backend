package coffeemeet.server.admin.service;

import static coffeemeet.server.user.domain.UserStatus.MATCHING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.implement.CertificationCommand;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

  @InjectMocks
  private AdminService adminService;

  @Mock
  private CertificationCommand certificationCommand;

  @Mock
  private UserQuery userQuery;

  @Mock
  private FCMNotificationSender fcmNotificationSender;

  @Mock
  private ReportCommand reportCommand;

  @Mock
  private MatchingQueueCommand matchingQueueCommand;

  @Mock
  private CertificationQuery certificationQuery;


  @Test
  @DisplayName("인증 수락 처리를 할 수 있다.")
  void approveCertificationTest() {
    // given
    Long userId = 1L;

    // when
    adminService.approveCertification(userId);

    // then
    then(certificationCommand).should(only()).certificated(userId);
    then(userQuery).should(only()).getNotificationInfoByUserId(userId);
    then(fcmNotificationSender).should(only()).sendNotification(any(), any());
  }

  @Test
  @DisplayName("인증 거절 처리를 할 수 있다.")
  void rejectCertificationTest() {
    // given
    Long userId = 1L;

    // when
    adminService.rejectCertification(userId);

    // then
    then(certificationCommand).should(only()).deleteUserCertification(userId);
    then(userQuery).should(only()).getNotificationInfoByUserId(userId);
    then(fcmNotificationSender).should(only()).sendNotification(any(), any());
  }

  @Test
  @DisplayName("신고 패널티를 부과하고 알림을 보낼 수 있다.")
  void punishUserTest() {
    // given
    Long userId = 1L;
    Long reportId = 1L;
    User user = mock(User.class);
    String companyName = "Company";

    willDoNothing().given(user).punished();
    given(user.getUserStatus()).willReturn(MATCHING);
    given(userQuery.getUserById(userId)).willReturn(user);
    given(certificationQuery.getCompanyNameByUserId(userId)).willReturn(companyName);

    // when
    adminService.punishUser(reportId, userId);

    // then
    then(matchingQueueCommand).should(only()).deleteUserByUserId(companyName, userId);
    then(reportCommand).should(only()).processReport(reportId);
    then(fcmNotificationSender).should(only()).sendNotification(any(), any());
  }

  @Test
  @DisplayName("신고 반려 처리를 할 수 있다.")
  void dismissReportTest() {
    // given
    Long reportId = 1L;

    // when
    adminService.dismissReport(reportId);

    // then
    then(reportCommand).should(only()).processReport(reportId);
  }

}
