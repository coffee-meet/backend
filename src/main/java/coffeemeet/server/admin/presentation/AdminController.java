package coffeemeet.server.admin.presentation;

import static coffeemeet.server.admin.exception.AdminErrorCode.NOT_AUTHORIZED;

import coffeemeet.server.admin.presentation.dto.AdminCustomPage;
import coffeemeet.server.admin.presentation.dto.AdminLoginHTTP;
import coffeemeet.server.admin.presentation.dto.CertificationApprovalHTTP;
import coffeemeet.server.admin.presentation.dto.CertificationRejectionHTTP;
import coffeemeet.server.admin.presentation.dto.ReportApprovalHTTP;
import coffeemeet.server.admin.presentation.dto.ReportRejectionHTTP;
import coffeemeet.server.admin.service.AdminService;
import coffeemeet.server.common.execption.InvalidAuthException;
import coffeemeet.server.inquiry.service.InquiryService;
import coffeemeet.server.inquiry.service.dto.InquirySearchResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

  private static final String REQUEST_WITHOUT_SESSION_MESSAGE = "SESSION 값이 존재하지 않습니다.";
  private static final String ADMIN_ID = "adminId";
  private final AdminService adminService;
  private final InquiryService inquiryService;

  @PostMapping("/login")
  public ResponseEntity<Void> login(
      HttpServletRequest httpServletRequest,
      @Valid @RequestBody AdminLoginHTTP.Request request
  ) {
    adminService.login(request.id(), request.password());

    HttpSession session = httpServletRequest.getSession();
    session.setAttribute(ADMIN_ID, request.id());
    session.setMaxInactiveInterval(1800);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      HttpServletRequest httpServletRequest
  ) {
    HttpSession session = httpServletRequest.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/certification/approval")
  public ResponseEntity<Void> approveCertification(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @Valid @RequestBody CertificationApprovalHTTP.Request request) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.approveCertification(request.userId());
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/certification/rejection")
  public ResponseEntity<Void> rejectCertification(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @Valid @RequestBody CertificationRejectionHTTP.Request request
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.rejectCertification(request.userId());
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/report/punishment")
  public ResponseEntity<Void> assignReportPenalty(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @Valid @RequestBody ReportApprovalHTTP.Request request
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.punishUser(request.reportId(), request.userId());
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/report/rejection")
  public ResponseEntity<Void> dismissReport(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @Valid @RequestBody ReportRejectionHTTP.Request request
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.dismissReport(request.reportId());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/inquiries")
  public ResponseEntity<AdminCustomPage<InquirySearchResponse.InquirySummary>> searchInquiries(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @RequestParam(defaultValue = "0") Long lastInquiryId,
      @RequestParam(defaultValue = "10") int pageSize) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    InquirySearchResponse inquiries = inquiryService.searchInquiries(lastInquiryId, pageSize);
    return ResponseEntity.ok(AdminCustomPage.of(inquiries.contents(), inquiries.hasNext()));
  }

}
