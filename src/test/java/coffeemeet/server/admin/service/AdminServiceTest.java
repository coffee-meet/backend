package coffeemeet.server.admin.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.admin.implement.AdminAccountValidator;
import coffeemeet.server.certification.implement.CertificationCommand;
import coffeemeet.server.common.domain.UserNotificationEvent;
import coffeemeet.server.common.fixture.InquiryFixture;
import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.implement.InquiryCommand;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.user.implement.UserCommand;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

  @InjectMocks
  private AdminService adminService;
  @Mock
  private CertificationCommand certificationCommand;
  @Mock
  private ApplicationEventPublisher applicationEventPublisher;
  @Mock
  private ReportCommand reportCommand;
  @Mock
  private UserCommand userCommand;
  @Mock
  private AdminAccountValidator adminAccountValidator;
  @Mock
  private InquiryCommand inquiryCommand;


  @Test
  @DisplayName("로그인할 수 있다.")
  void loginTest() {
    // given
    String id = "1";
    String password = "1";

    // when
    adminService.login(id, password);

    //  then
    then(adminAccountValidator).should(only()).validate(id, password);
  }

  @Test
  @DisplayName("인증 수락 처리를 할 수 있다.")
  void approveCertificationTest() {
    // given
    Long certificationId = 1L;

    // when
    adminService.approveCertification(certificationId);

    // then
    then(certificationCommand).should(only()).completeCertification(certificationId);
    then(applicationEventPublisher).should(only()).publishEvent(any(UserNotificationEvent.class));
  }

  @Test
  @DisplayName("인증 거절 처리를 할 수 있다.")
  void rejectCertificationTest() {
    // given
    Long certificationId = 1L;

    // when
    adminService.rejectCertification(certificationId);

    // then
    then(certificationCommand).should(only()).deleteCertificationByUserId(certificationId);
    then(applicationEventPublisher).should(only()).publishEvent(any(UserNotificationEvent.class));
  }

  @Test
  @DisplayName("신고를 승인할 수 있다.")
  void approveReportTest() {
    // given
    Long userId = 1L;
    Set<Long> reportIds = Set.of(1L, 2L, 3L, 4L, 5L);

    // when
    adminService.approveReport(1L, reportIds);

    // then
    then(userCommand).should(only()).updatePunishedUser(userId);
    then(reportCommand).should(only()).processReports(reportIds);
    then(applicationEventPublisher).should(only()).publishEvent(any(UserNotificationEvent.class));
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
    Long inquiryId = 1L;

    // when
    adminService.checkInquiry(inquiryId);

    // then
    then(inquiryCommand).should(only()).updateCheckedInquiry(inquiryId);
  }

}
