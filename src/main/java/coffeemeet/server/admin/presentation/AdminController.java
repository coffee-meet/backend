package coffeemeet.server.admin.presentation;

import static coffeemeet.server.admin.exception.AdminErrorCode.NOT_AUTHORIZED;

import coffeemeet.server.admin.presentation.dto.AdminLoginHTTP;
import coffeemeet.server.admin.presentation.dto.ReportDeletionHTTP;
import coffeemeet.server.admin.presentation.dto.UserPunishmentHTTP;
import coffeemeet.server.admin.service.AdminService;
import coffeemeet.server.common.execption.InvalidAuthException;
import coffeemeet.server.report.presentation.dto.ReportDetailHTTP;
import coffeemeet.server.report.presentation.dto.ReportHTTP;
import coffeemeet.server.report.presentation.dto.TargetReportHTTP;
import coffeemeet.server.report.service.ReportService;
import coffeemeet.server.report.service.dto.ReportDetailDto;
import coffeemeet.server.report.service.dto.ReportDto.Response;
import coffeemeet.server.report.service.dto.TargetReportDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @PostMapping("/login")
  public ResponseEntity<Void> login(
      HttpServletRequest httpServletRequest,
      @Valid @RequestBody AdminLoginHTTP.Request request
  ) {
    adminService.login(request.id(), request.password());

    HttpSession session = httpServletRequest.getSession();
    session.setAttribute(ADMIN_SESSION_ATTRIBUTE, request.id());
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

  @PatchMapping("/certifications/{certificationId}/approval")
  public ResponseEntity<Void> approveCertification(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @PathVariable Long certificationId) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.approveCertification(certificationId);
    return ResponseEntity.ok().build();
  } // 완

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

  @PatchMapping("/users/{userId}/punishment")
  public ResponseEntity<Void> assignReportPenalty(
      @SessionAttribute(name = ADMIN_SESSION_ATTRIBUTE, required = false) String adminId,
      @PathVariable Long userId,
      @Valid @RequestBody UserPunishmentHTTP.Request request
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    adminService.punishUser(userId, request.reportIds());
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
  public ResponseEntity<Page<ReportHTTP.Response>> findAllReports(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @PageableDefault Pageable pageable
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    Page<Response> reportPage = reportService.findAllReports(pageable);
    Page<ReportHTTP.Response> responsePage = reportPage.map(ReportHTTP.Response::from);
    return ResponseEntity.ok(responsePage);
  }

  @GetMapping("/reports/group")
  public ResponseEntity<List<TargetReportHTTP.Response>> findReportByTargetIdAndChattingRoomId(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @RequestParam Long chattingRoomId,
      @RequestParam Long targetedId
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    List<TargetReportDto.Response> response = reportService.findReportByTargetIdAndChattingRoomId(
        targetedId, chattingRoomId);
    return ResponseEntity.ok(response.stream()
        .map(TargetReportHTTP.Response::from)
        .toList());
  }

  @GetMapping("/reports/detail/{reportId}")
  public ResponseEntity<ReportDetailHTTP.Response> findReport(
      @SessionAttribute(name = ADMIN_ID, required = false) String adminId,
      @PathVariable Long reportId
  ) {
    if (adminId == null) {
      throw new InvalidAuthException(NOT_AUTHORIZED, REQUEST_WITHOUT_SESSION_MESSAGE);
    }
    ReportDetailDto.Response response = reportService.findReportById(reportId);
    return ResponseEntity.ok(ReportDetailHTTP.Response.from(response));
  }

}
