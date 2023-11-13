package coffeemeet.server.chatting.current.presentation.dto;

import coffeemeet.server.chatting.current.service.dto.ChattingDto;
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
      Long messageId,
      String nickname,
      String content,
      LocalDateTime createdAt
  ) implements ChatStomp {

    public static ChatStomp.Response from(ChattingDto.Response response) {
      return new ChatStomp.Response(
          response.messageId(),
          response.nickname(),
          response.content(),
          response.createdAt()
      );
    }

  }

}
