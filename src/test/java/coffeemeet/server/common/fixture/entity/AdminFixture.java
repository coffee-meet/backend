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
import coffeemeet.server.admin.presentation.dto.ReportDeletionHTTP;
import coffeemeet.server.admin.presentation.dto.UserPunishmentHTTP;
import org.instancio.Instancio;

public class AdminFixture {


  public static AdminLoginHTTP.Request adminLoginHTTPRequest() {
    return Instancio.of(AdminLoginHTTP.Request.class).create();
  }

  public static UserPunishmentHTTP.Request reportApprovalHTTPRequest() {
    return Instancio.of(UserPunishmentHTTP.Request.class).create();
  }

  public static ReportDeletionHTTP.Request reportRejectionHTTPRequest() {
    return Instancio.of(ReportDeletionHTTP.Request.class).create();
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
