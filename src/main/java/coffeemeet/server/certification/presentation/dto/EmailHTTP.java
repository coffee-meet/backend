package coffeemeet.server.certification.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public sealed interface EmailHTTP permits EmailHTTP.Request {

  record Request(
      @Email @NotNull
      String companyEmail
  ) implements EmailHTTP {

  }

}
