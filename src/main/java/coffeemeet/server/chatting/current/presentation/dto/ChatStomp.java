package coffeemeet.server.chatting.current.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public sealed interface ChatStomp permits ChatStomp.Request, ChatStomp.Response {

  record Request(
      @NotNull
      Long roomId,
      @NotBlank
      String content
  ) implements ChatStomp {

  }

  record Response(
      String nickname,
      String content,
      LocalDateTime createdAt
  ) implements ChatStomp {

  }

}
