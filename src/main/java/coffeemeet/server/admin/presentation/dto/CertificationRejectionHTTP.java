package coffeemeet.server.admin.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CertificationRejectionHTTP {

  public record Request(
      @NotNull
      Long userId
  ) {

  }

}
