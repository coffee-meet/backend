package coffeemeet.server.certification.presentation.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public sealed interface VerificationCodeHTTP permits VerificationCodeHTTP.Request {

  record Request(
      @NotNull @Length(min = 6, max = 6)
      String verificationCode
  ) implements VerificationCodeHTTP {

  }

}
