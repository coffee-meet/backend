package coffeemeet.server.certification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record EmailDto(
    @Email @NotNull
    String companyEmail
) {

}
