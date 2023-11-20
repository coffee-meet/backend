package coffeemeet.server.admin.presentation.dto;

import jakarta.validation.constraints.NotNull;

public sealed interface ReportApprovalHTTP permits ReportApprovalHTTP.Request {

  record Request(
      @NotNull
      Long reportId,
      @NotNull
      Long userId
  ) implements ReportApprovalHTTP {

  }

}
