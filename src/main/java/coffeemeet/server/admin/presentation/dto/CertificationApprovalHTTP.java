package coffeemeet.server.admin.presentation.dto;

public sealed interface CertificationApprovalHTTP permits CertificationApprovalHTTP.Request {

  record Request(
      Long userId
  ) implements CertificationApprovalHTTP {

  }

}
