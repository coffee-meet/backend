package coffeemeet.server.admin.presentation.dto;

import jakarta.validation.constraints.NotNull;

public sealed interface ReportRejectionHTTP permits ReportRejectionHTTP.Request {

  record Request(
      @NotNull
      Long reportId
  ) implements ReportRejectionHTTP {

  }

}
