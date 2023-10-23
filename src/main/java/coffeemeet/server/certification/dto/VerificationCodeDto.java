package coffeemeet.server.certification.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record VerificationCodeDto(
    @NotNull @Length(min = 6, max = 6)
    String verificationCode
) {

}
