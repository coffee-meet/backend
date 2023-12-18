package coffeemeet.server.admin.service;

import static coffeemeet.server.user.domain.UserStatus.MATCHING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;

import coffeemeet.server.admin.implement.AdminQuery;
import coffeemeet.server.certification.implement.CertificationCommand;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.common.fixture.InquiryFixture;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.implement.InquiryCommand;
import coffeemeet.server.inquiry.implement.InquiryQuery;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.user.domain.User;
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

  @Mock
  private InquiryQuery inquiryQuery;

  @Mock
  private InquiryCommand inquiryCommand;

  @Mock
  private AdminQuery adminQuery;

  @Test
  @DisplayName("로그인할 수 있다.")
  void loginTest() {
    // given
    String id = "1";
    String password = "1";

    willDoNothing().given(adminQuery).checkIdAndPassword(anyString(), anyString());

    // when, then
    assertThatCode(() -> adminService.login(id, password))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("인증 수락 처리를 할 수 있다.")
  void approveCertificationTest() {
    // given
    Long certificationId = 1L;
    Long userId = 1L;

    given(certificationQuery.getUserIdByCertificationId(certificationId)).willReturn(userId);

    // when
    adminService.approveCertification(certificationId);

    // then
    then(certificationCommand).should(only()).certificated(certificationId);
    then(userQuery).should(only()).getNotificationInfoByUserId(userId);
    then(fcmNotificationSender).should(only()).sendNotification(any(), any());
  }

  @Test
  @DisplayName("인증 거절 처리를 할 수 있다.")
  void rejectCertificationTest() {
    // given
    Long certificationId = 1L;
    Long userId = 1L;
    given(certificationQuery.getUserIdByCertificationId(certificationId)).willReturn(userId);

    // when
    adminService.rejectCertification(certificationId);

    // then
    then(certificationCommand).should(only()).deleteCertification(certificationId);
    then(certificationQuery).should(only()).getUserIdByCertificationId(certificationId);
    then(userQuery).should(only()).getNotificationInfoByUserId(certificationId);
    then(fcmNotificationSender).should(only()).sendNotification(any(), any());
  }

  @Test
  @DisplayName("매칭 중인 유저를 매칭 큐에서 제외하고 신고 패널티를 부과하고 알림을 보낼 수 있다.")
  void punishUserTest() {
    // given
    Long userId = 1L;
    Set<Long> reportIds = Set.of(1L, 2L, 3L, 4L, 5L);
    User user = mock(User.class);
    String companyName = "Company";

    willDoNothing().given(user).punished();
    given(user.getUserStatus()).willReturn(MATCHING);
    given(userQuery.getUserById(userId)).willReturn(user);
    given(certificationQuery.getCompanyNameByUserId(userId)).willReturn(companyName);

    // when
    adminService.punishUser(userId, reportIds);

    // then
    then(matchingQueueCommand).should(only()).deleteUserByUserId(companyName, userId);
    then(reportCommand).should(only()).processReports(new HashSet<>(reportIds));
    then(fcmNotificationSender).should(only()).sendNotification(any(), any());
  }

  @Test
  @DisplayName("신고 반려 처리를 할 수 있다.")
  void dismissReportTest() {
    // given
    Set<Long> reportIds = Set.of(1L, 2L, 3L, 4L, 5L);

    // when
    adminService.dismissReport(reportIds);

    // then
    then(reportCommand).should(only()).deleteReports(reportIds);
  }

  @Test
  @DisplayName("문의 확인 처리를 할 수 있다.")
  void checkInquiryTest() {
    // given
    Inquiry inquiry = InquiryFixture.inquiry();
    given(inquiryQuery.getInquiryBy(anyLong())).willReturn(inquiry);

    // when
    adminService.checkInquiry(inquiry.getInquirerId());

    // then
    then(inquiryCommand).should(only()).check(inquiry);
    then(userQuery).should(only()).getNotificationInfoByUserId(inquiry.getInquirerId());
    then(fcmNotificationSender).should(only()).sendNotification(any(), any());
  }

}
