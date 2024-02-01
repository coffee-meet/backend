package coffeemeet.server.admin.service;

import coffeemeet.server.admin.implement.AdminAccountValidator;
import coffeemeet.server.certification.implement.CertificationCommand;
import coffeemeet.server.common.infrastructure.FCMNotificationSender;
import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.implement.InquiryCommand;
import coffeemeet.server.inquiry.implement.InquiryQuery;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final CertificationCommand certificationCommand;
  private final UserQuery userQuery;
  private final FCMNotificationSender fcmNotificationSender;
  private final ReportCommand reportCommand;
  private final UserCommand userCommand;
  private final AdminAccountValidator adminAccountValidator;
  private final InquiryQuery inquiryQuery;
  private final InquiryCommand inquiryCommand;

  @Transactional(readOnly = true)
  public void login(String id, String password) {
    adminAccountValidator.validate(id, password);
  }

  @Transactional
  public void approveCertification(Long certificationId) {
    certificationCommand.completeCertification(certificationId);

    NotificationInfo notificationInfo = userQuery.getNotificationInfoByUserId(certificationId);
    fcmNotificationSender.sendNotification(notificationInfo,
        "축하드립니다! 회사 인증이 완료됐습니다. 이제부터 모든 서비스를 자유롭게 이용하실 수 있습니다.");
  }

  @Transactional
  public void rejectCertification(Long certificationId) {
    certificationCommand.deleteCertificationByUserId(certificationId);

    NotificationInfo notificationInfo = userQuery.getNotificationInfoByUserId(certificationId);
    fcmNotificationSender.sendNotification(notificationInfo, "규정 및 기준에 부합하지 않아 회사 인증이 반려되었습니다.");
  }

  @Transactional
  public void punishUser(Long userId, Set<Long> reportIds) {
    userCommand.updatePunishedUser(userId);
    reportCommand.processReports(reportIds);

    NotificationInfo notificationInfo = userQuery.getNotificationInfoByUserId(userId);
    fcmNotificationSender.sendNotification(notificationInfo,
        "귀하의 계정은 최근 신고 접수로 인해 일시적으로 서비스 이용이 제한되었습니다.");
  }

  @Transactional
  public void dismissReport(Set<Long> reportIds) {
    reportCommand.deleteReports(reportIds);
  }

  public void checkInquiry(Long inquiryId) {
    Inquiry inquiry = inquiryQuery.getInquiryBy(inquiryId);
    inquiryCommand.check(inquiry);

    NotificationInfo notificationInfo = userQuery.getNotificationInfoByUserId(
        inquiry.getInquirerId());
    fcmNotificationSender.sendNotification(notificationInfo, "작성하신 문의가 확인되었습니다. 계정 메일을 확인해주세요");
  }

}
