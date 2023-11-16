package coffeemeet.server.admin.presentation.dto;

public sealed interface ReportApprovalHTTP permits ReportApprovalHTTP.Request {

  record Request(
      Long reportId,
      Long userId
  ) implements ReportApprovalHTTP {

  }

}
