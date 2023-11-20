package coffeemeet.server.certification.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor(access = PRIVATE)
public final class VerificationCodeHTTP {

  public record Request(
      @NotNull @Length(min = 6, max = 6)
      String verificationCode
  ) {

  }

}
