package coffeemeet.server.common.fixture;

import static org.instancio.Select.field;

import coffeemeet.server.admin.presentation.dto.AdminCustomSlice;
import coffeemeet.server.admin.presentation.dto.AdminLoginHTTP;
import coffeemeet.server.admin.presentation.dto.ReportDeletionHTTP;
import coffeemeet.server.admin.presentation.dto.UserPunishmentHTTP;
import coffeemeet.server.inquiry.service.dto.InquirySummaryDto;
import java.util.List;
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

  @SuppressWarnings("unchecked")
  public static AdminCustomSlice<InquirySummaryDto> adminCustomPageByInquiry(
      List<InquirySummaryDto> contents, boolean hasNext) {
    return Instancio.of(AdminCustomSlice.class)
        .withTypeParameters(InquirySummaryDto.class)
        .set(field(AdminCustomSlice<InquirySummaryDto>::contents), contents)
        .set(field(AdminCustomSlice<InquirySummaryDto>::hasNext), hasNext)
        .create();
  }

}
