package coffeemeet.server.admin.presentation.dto;

import jakarta.validation.constraints.NotNull;

public sealed interface CertificationApprovalHTTP permits CertificationApprovalHTTP.Request {

  record Request(
      @NotNull
      Long userId
  ) implements CertificationApprovalHTTP {

  }

}
