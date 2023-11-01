package coffeemeet.server.certification.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public sealed interface EmailDto permits EmailDto.Request {

  record Request(
      @Email @NotNull
      String companyEmail
  ) implements EmailDto {

  }

}
