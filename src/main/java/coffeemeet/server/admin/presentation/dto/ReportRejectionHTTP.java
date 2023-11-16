package coffeemeet.server.admin.presentation.dto;

public sealed interface ReportRejectionHTTP permits ReportRejectionHTTP.Request {

  record Request(
      Long reportId
  ) implements ReportRejectionHTTP {

  }

}
