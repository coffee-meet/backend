package coffeemeet.server.common.fixture.entity;

import coffeemeet.server.admin.presentation.dto.AdminLoginHTTP;
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

}
