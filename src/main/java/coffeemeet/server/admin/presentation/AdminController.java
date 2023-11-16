package coffeemeet.server.admin.presentation;

import coffeemeet.server.admin.presentation.dto.CertificationApprovalHTTP;
import coffeemeet.server.admin.presentation.dto.CertificationRejectionHTTP;
import coffeemeet.server.admin.presentation.dto.ReportApprovalHTTP;
import coffeemeet.server.admin.presentation.dto.ReportRejectionHTTP;
import coffeemeet.server.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

  private final AdminService adminService;

  @PatchMapping("/certification/approval")
  public void approveCertification(@RequestBody CertificationApprovalHTTP.Request request) {
    adminService.approveCertification(request.userId());
  }

  @PatchMapping("/certification/rejection")
  public void rejectCertification(@RequestBody CertificationRejectionHTTP.Request request) {
    adminService.rejectCertification(request.userId());
  }

  @PatchMapping("/report/punishment")
  public void assignReportPenalty(@RequestBody ReportApprovalHTTP.Request request) {
    adminService.punishUser(request.reportId(), request.userId());
  }

  @PatchMapping("/report/rejection")
  public void dismissReport(@RequestBody ReportRejectionHTTP.Request request) {
    adminService.dismissReport(request.reportId());
  }

}
