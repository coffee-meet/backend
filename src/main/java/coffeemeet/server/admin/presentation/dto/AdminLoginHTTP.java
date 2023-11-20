package coffeemeet.server.admin.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class AdminLoginHTTP {

  public record Request(
      @NotBlank
      String id,
      @NotBlank
      String password
  ) {

  }

}
