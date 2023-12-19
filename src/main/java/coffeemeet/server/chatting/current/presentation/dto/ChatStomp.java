package coffeemeet.server.chatting.current.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.chatting.current.service.dto.Chatting;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ChatStomp {

  public record Request(
      @NotNull
      Long roomId,
      @NotBlank
      String content
  ) {

  }

  public record Response(
      Long userId,
      Long messageId,
      String nickname,
      String content,
      String profileImageUrl,
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
      LocalDateTime createdAt
  ) {

    public static ChatStomp.Response from(Chatting response) {
      return new ChatStomp.Response(
          response.userId(),
          response.messageId(),
          response.nickname(),
          response.content(),
          response.profileImageUrl(),
          response.createdAt()
      );
    }

  }

}
