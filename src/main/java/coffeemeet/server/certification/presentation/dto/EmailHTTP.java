package coffeemeet.server.certification.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class EmailHTTP {

  public record Request(
      @Email @NotNull String companyEmail
  ) {

  }

}
