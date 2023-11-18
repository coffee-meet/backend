package coffeemeet.server.admin.presentation;

import static coffeemeet.server.admin.exception.AdminErrorCode.NOT_AUTHORIZED;

import coffeemeet.server.admin.presentation.dto.CertificationApprovalHTTP;
import coffeemeet.server.admin.presentation.dto.CertificationRejectionHTTP;
import coffeemeet.server.admin.presentation.dto.ReportApprovalHTTP;
import coffeemeet.server.admin.presentation.dto.ReportRejectionHTTP;
import coffeemeet.server.admin.service.AdminService;
import coffeemeet.server.common.execption.InvalidAuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

  public static final String REQUEST_WITHOUT_SESSION_MESSAGE = "SESSION 값이 존재하지 않습니다.";
  public static final String ADMIN_ID = "adminId";
  private final AdminService adminService;

  @PostMapping("/login")
  public void login(
      HttpServletRequest httpServletRequest,
      @RequestBody AdminLoginHTTP.Request request
  ) {
    adminService.login(request.id(), request.password());

    HttpSession session = httpServletRequest.getSession();
    session.setAttribute(ADMIN_ID, request.id());
    session.setMaxInactiveInterval(1800);
  }

  @PostMapping("/logout")
  public void logout(
      HttpServletRequest httpServletRequest
  ) {
    HttpSession session = httpServletRequest.getSession(false);
    if (session != null) {
      session.invalidate();
    }
  }

  @PatchMapping("/certification/approval")
  public void approveCertification(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @RequestBody CertificationApprovalHTTP.Request request) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.approveCertification(request.userId());
  }

  @PatchMapping("/certification/rejection")
  public void rejectCertification(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @RequestBody CertificationRejectionHTTP.Request request
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.rejectCertification(request.userId());
  }

  @PatchMapping("/report/punishment")
  public void assignReportPenalty(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @RequestBody ReportApprovalHTTP.Request request
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.punishUser(request.reportId(), request.userId());
  }

  @PatchMapping("/report/rejection")
  public void dismissReport(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @RequestBody ReportRejectionHTTP.Request request
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.dismissReport(request.reportId());
  }

}
