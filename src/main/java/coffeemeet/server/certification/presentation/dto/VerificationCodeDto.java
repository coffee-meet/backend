package coffeemeet.server.certification.presentation.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public sealed interface VerificationCodeDto permits VerificationCodeDto.Request {

  record Request(
      @NotNull @Length(min = 6, max = 6)
      String verificationCode
  ) implements VerificationCodeDto {

  }

}
