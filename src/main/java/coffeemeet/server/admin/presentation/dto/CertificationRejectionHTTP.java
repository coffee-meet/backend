package coffeemeet.server.admin.presentation.dto;

public sealed interface CertificationRejectionHTTP permits CertificationRejectionHTTP.Request {

  record Request(
      Long userId
  ) implements CertificationRejectionHTTP {

  }

}
