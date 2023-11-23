package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.admin.presentation.dto.AdminCustomPage;
import coffeemeet.server.admin.presentation.dto.AdminLoginHTTP;
import coffeemeet.server.admin.presentation.dto.CertificationApprovalHTTP;
import coffeemeet.server.admin.presentation.dto.CertificationRejectionHTTP;
import coffeemeet.server.admin.presentation.dto.ReportApprovalHTTP;
import coffeemeet.server.admin.presentation.dto.ReportRejectionHTTP;
import coffeemeet.server.inquiry.service.dto.InquirySearchResponse;
import coffeemeet.server.inquiry.service.dto.InquirySearchResponse.InquirySummary;
import java.util.List;
import org.instancio.Instancio;

public class AdminFixture {


  public static AdminLoginHTTP.Request adminLoginHTTPRequest() {
    return Instancio.of(AdminLoginHTTP.Request.class).create();
  }

  public static CertificationApprovalHTTP.Request certificationApprovalHTTPRequest() {
    return Instancio.of(CertificationApprovalHTTP.Request.class).create();
  }

  public static CertificationRejectionHTTP.Request certificationRejectionHTTPRequest() {
    return Instancio.of(CertificationRejectionHTTP.Request.class).create();
  }

  public static ReportApprovalHTTP.Request reportApprovalHTTPRequest() {
    return Instancio.of(ReportApprovalHTTP.Request.class).create();
  }

  public static ReportRejectionHTTP.Request reportRejectionHTTPRequest() {
    return Instancio.of(ReportRejectionHTTP.Request.class).create();
  }

  public static AdminCustomPage<InquirySearchResponse.InquirySummary> adminCustomPageByInquiry(
      List<InquirySummary> contents, boolean hasNext) {
    return Instancio.of(AdminCustomPage.class)
        .withTypeParameters(InquirySearchResponse.InquirySummary.class)
        .set(field(AdminCustomPage<InquirySummary>::contents), contents)
        .set(field(AdminCustomPage<InquirySummary>::hasNext), hasNext)
        .create();
  }

}
