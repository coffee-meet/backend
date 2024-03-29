package coffeemeet.server.admin.presentation;

import static coffeemeet.server.admin.exception.AdminErrorCode.NOT_AUTHORIZED;

import coffeemeet.server.admin.presentation.dto.AdminCustomPage;
import coffeemeet.server.admin.presentation.dto.AdminCustomSlice;
import coffeemeet.server.admin.presentation.dto.AdminLoginHTTP;
import coffeemeet.server.admin.presentation.dto.FindGroupReportsParam;
import coffeemeet.server.admin.presentation.dto.GroupReportHTTP;
import coffeemeet.server.admin.presentation.dto.ReportDeletionHTTP;
import coffeemeet.server.admin.presentation.dto.ReportDetailHTTP;
import coffeemeet.server.admin.presentation.dto.UserPunishmentHTTP;
import coffeemeet.server.admin.service.AdminService;
import coffeemeet.server.certification.service.CertificationService;
import coffeemeet.server.certification.service.dto.PendingCertification;
import coffeemeet.server.certification.service.dto.PendingCertificationPageDto;
import coffeemeet.server.common.execption.InvalidAuthException;
import coffeemeet.server.inquiry.presentation.dto.InquiryDetailHTTP;
import coffeemeet.server.inquiry.service.InquiryService;
import coffeemeet.server.inquiry.service.dto.InquiryDetailDto;
import coffeemeet.server.inquiry.service.dto.InquirySearchDto;
import coffeemeet.server.inquiry.service.dto.InquirySummary;
import coffeemeet.server.report.service.ReportService;
import coffeemeet.server.report.service.dto.GroupReportDto;
import coffeemeet.server.report.service.dto.ReportDetailDto;
import coffeemeet.server.report.service.dto.ReportListDto;
import coffeemeet.server.report.service.dto.ReportSummary;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdminController {

  private static final String REQUEST_WITHOUT_SESSION_MESSAGE = "SESSION 값이 존재하지 않습니다.";
  private static final String ADMIN_SESSION_ATTRIBUTE = "adminId";
  private final AdminService adminService;
  private final ReportService reportService;
  private final InquiryService inquiryService;
  private final CertificationService certificationService;

  @PostMapping("/login")
  public ResponseEntity<Void> login(
      HttpServletRequest httpServletRequest,
      @Valid @RequestBody AdminLoginHTTP.Request request
  ) {
    adminService.login(request.id(), request.password());
    HttpSession session = httpServletRequest.getSession();
    session.setAttribute(ADMIN_SESSION_ATTRIBUTE, request.id());
    session.setMaxInactiveInterval(3600);
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

  @PatchMapping("/certifications/{certificationId}/approval")
  public ResponseEntity<Void> approveCertification(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @PathVariable Long certificationId) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.approveCertification(certificationId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/certifications/{certificationId}/rejection")
  public ResponseEntity<Void> rejectCertification(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @PathVariable Long certificationId) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.rejectCertification(certificationId);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/users/{targetedId}/punishment")
  public ResponseEntity<Void> assignReportPenalty(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @PathVariable Long targetedId,
      @Valid @RequestBody UserPunishmentHTTP.Request request
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.approveReport(targetedId, request.reportIds());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/reports")
  public ResponseEntity<Void> dismissReport(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @Valid @RequestBody ReportDeletionHTTP.Request request
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.dismissReport(request.reportIds());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/reports")
  public ResponseEntity<AdminCustomSlice<ReportSummary>> findAllReports(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @RequestParam(defaultValue = "0") Long lastReportId,
      @RequestParam(defaultValue = "10") int pageSize
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    ReportListDto reportListDto = reportService.findAllReports(lastReportId, pageSize);
    return ResponseEntity.ok(
        AdminCustomSlice.of(reportListDto.contents(), reportListDto.hasNext()));
  }

  @GetMapping("/reports/group")
  public ResponseEntity<GroupReportHTTP.Response> findReportByTargetIdAndChattingRoomId(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @ModelAttribute FindGroupReportsParam findGroupReportsParam
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    List<GroupReportDto> responses = reportService.findReportByTargetIdAndChattingRoomId(
        findGroupReportsParam.targetedId(), findGroupReportsParam.chattingRoomId());
    return ResponseEntity.ok(GroupReportHTTP.Response.from(responses));
  }

  @GetMapping("/reports/detail/{reportId}")
  public ResponseEntity<ReportDetailHTTP.Response> findReport(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @PathVariable Long reportId
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    ReportDetailDto response = reportService.findReportById(reportId);
    return ResponseEntity.ok(ReportDetailHTTP.Response.from(response));
  }

  @GetMapping("/inquiries")
  public ResponseEntity<AdminCustomSlice<InquirySummary>> searchInquiries(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @RequestParam(defaultValue = "0") Long lastInquiryId,
      @RequestParam(defaultValue = "10") int pageSize) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    InquirySearchDto inquiries = inquiryService.searchInquiries(lastInquiryId, pageSize);
    return ResponseEntity.ok(AdminCustomSlice.of(inquiries.contents(), inquiries.hasNext()));
  }

  @GetMapping("/inquiries/detail/{inquiryId}")
  public ResponseEntity<InquiryDetailHTTP.Response> viewInquiry(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @PathVariable Long inquiryId
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    InquiryDetailDto response = inquiryService.findInquiryBy(inquiryId);
    return ResponseEntity.ok(InquiryDetailHTTP.Response.from(response));
  }

  @PatchMapping("/inquiries/{inquiryId}/check")
  public ResponseEntity<Void> checkInquiry(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @PathVariable Long inquiryId) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.checkInquiry(inquiryId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/certifications/pending")
  public ResponseEntity<AdminCustomPage<PendingCertification>> getPendingCertifications(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int size
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }

    int pageNumber = offset / size;
    Pageable pageable = PageRequest.of(pageNumber, size);
    PendingCertificationPageDto uncertifiedUserRequests = certificationService.getUncertifiedUserRequests(
        pageable);

    Page<PendingCertification> page = uncertifiedUserRequests.page();
    return ResponseEntity.ok(
        AdminCustomPage.of(page.getContent(), page.hasNext())
    );
  }

}
