package coffeemeet.server.matching.presentation.dto;

import jakarta.validation.constraints.NotNull;

public sealed interface AcceptResponseHttp permits AcceptResponseHttp.Request {

  record Request(
      @NotNull
      Long userId,
      @NotNull
      boolean isAccepted,
      @NotNull
      String waitingRoomId
  ) implements AcceptResponseHttp {

  }

}
