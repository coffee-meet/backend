package coffeemeet.server.chatting.current.service.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ChattingDto {

  public record Response(
      Long userId,
      Long messageId,
      String nickname,
      String content,
      String profileImageUrl,
      LocalDateTime createdAt
  ) {

    public static Response of(User user, ChattingMessage chattingMessage) {
      return new Response(
          user.getId(),
          chattingMessage.getId(),
          user.getProfile().getNickname(),
          chattingMessage.getMessage(),
          user.getOauthInfo().getProfileImageUrl(),
          chattingMessage.getCreatedAt()
      );
    }

  }

}
