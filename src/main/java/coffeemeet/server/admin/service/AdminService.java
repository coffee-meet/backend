package coffeemeet.server.admin.service;

import coffeemeet.server.admin.domain.UserNotificationEvent;
import coffeemeet.server.admin.implement.AdminAccountValidator;
import coffeemeet.server.certification.implement.CertificationCommand;
import coffeemeet.server.inquiry.implement.InquiryCommand;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.user.implement.UserCommand;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final CertificationCommand certificationCommand;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final ReportCommand reportCommand;
  private final UserCommand userCommand;
  private final AdminAccountValidator adminAccountValidator;
  private final InquiryCommand inquiryCommand;

  @Transactional(readOnly = true)
  public void login(String id, String password) {
    adminAccountValidator.validate(id, password);
  }

  @Transactional
  public void approveCertification(Long certificationId) {
    certificationCommand.completeCertification(certificationId);
    applicationEventPublisher.publishEvent(new UserNotificationEvent(certificationId,
        "축하드립니다! 회사 인증이 완료됐습니다. 이제부터 모든 서비스를 자유롭게 이용하실 수 있습니다."));
  }

  @Transactional
  public void rejectCertification(Long certificationId) {
    certificationCommand.deleteCertificationByUserId(certificationId);
    applicationEventPublisher.publishEvent(
        new UserNotificationEvent(certificationId, "규정 및 기준에 부합하지 않아 회사 인증이 반려되었습니다."));
  }

  @Transactional
  public void approveReport(Long userId, Set<Long> reportIds) {
    userCommand.updatePunishedUser(userId);
    reportCommand.processReports(reportIds);
    applicationEventPublisher.publishEvent(
        new UserNotificationEvent(userId, "귀하의 계정은 최근 신고 접수로 인해 일시적으로 서비스 이용이 제한되었습니다."));
  }

  @Transactional
  public void dismissReport(Set<Long> reportIds) {
    reportCommand.deleteReports(reportIds);
  }

  @Transactional
  public void checkInquiry(Long inquiryId) {
    inquiryCommand.updateCheckedInquiry(inquiryId);
    // TODO: 2024/02/02 문의 응답 작성 및 (이메일 전송 or 알림 보내고 추후 확인)
  }

}
