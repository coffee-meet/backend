package coffeemeet.server.admin.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public sealed interface AdminLoginHTTP permits AdminLoginHTTP.Request {

  record Request(
      @NotBlank
      String id,
      @NotBlank
      String password
  ) implements AdminLoginHTTP {

  }

}
