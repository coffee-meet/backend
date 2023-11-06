package coffeemeet.server.chatting.current.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public sealed interface ChatStomp permits ChatStomp.Request {

  record Request(
      @NotNull
      Long roomId,
      @NotBlank
      String content
  ) implements ChatStomp {

  }

}
