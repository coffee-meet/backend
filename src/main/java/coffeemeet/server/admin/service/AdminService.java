package coffeemeet.server.admin.service;

import static coffeemeet.server.user.domain.UserStatus.MATCHING;

import coffeemeet.server.admin.implement.AdminQuery;
import coffeemeet.server.certification.implement.CertificationCommand;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.report.implement.ReportCommand;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.User;
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
  private final MatchingQueueCommand matchingQueueCommand;
  private final CertificationQuery certificationQuery;
  private final AdminQuery adminQuery;

  public void login(String id, String password) {
    adminQuery.checkIdAndPassword(id, password);
  }

  public void approveCertification(Long certificationId) {
    certificationCommand.certificated(certificationId);

    Long userId = certificationQuery.getUserIdByCertificationId(certificationId);
    NotificationInfo notificationInfo = userQuery.getNotificationInfoByUserId(userId);

    fcmNotificationSender.sendNotification(notificationInfo,
        "축하드립니다! 회사 인증이 완료됐습니다. 이제부터 모든 서비스를 자유롭게 이용하실 수 있습니다.");
  }

  public void rejectCertification(Long certificationId) {
    certificationCommand.deleteCertification(certificationId);

    Long userId = certificationQuery.getUserIdByCertificationId(certificationId);
    NotificationInfo notificationInfo = userQuery.getNotificationInfoByUserId(userId);

    fcmNotificationSender.sendNotification(notificationInfo, "규정 및 기준에 부합하지 않아 회사 인증이 반려되었습니다.");
  }

  @Transactional  // TODO: 2023/11/17 추후 구조 변경을 통해서 개선 예정
  public void punishUser(Long userId, Set<Long> reportIds) {
    User user = userQuery.getUserById(userId);
    if (user.getUserStatus() == MATCHING) {
      String companyName = certificationQuery.getCompanyNameByUserId(userId);
      matchingQueueCommand.deleteUserByUserId(companyName, userId);
    }
    user.punished();

    reportCommand.processReports(reportIds);
    fcmNotificationSender.sendNotification(user.getNotificationInfo(),
        "귀하의 계정은 최근 신고 접수로 인해 일시적으로 서비스 이용이 제한되었습니다.");
  }

  public void dismissReport(Set<Long> reportIds) {
    reportCommand.deleteReports(reportIds);
  }

}
