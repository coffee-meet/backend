package coffeemeet.server.user.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public sealed interface NotificationTokenHTTP permits NotificationTokenHTTP.Request {

  record Request(
      @Email @NotNull
      String token
  ) implements NotificationTokenHTTP {

  }

}
