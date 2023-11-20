package coffeemeet.server.admin.presentation.dto;

import jakarta.validation.constraints.NotNull;

public sealed interface CertificationRejectionHTTP permits CertificationRejectionHTTP.Request {

  record Request(
      @NotNull
      Long userId
  ) implements CertificationRejectionHTTP {

  }

}
