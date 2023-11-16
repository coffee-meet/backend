package coffeemeet.server.chatting.current.service.dto;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public sealed interface ChattingDto permits ChattingDto.Response {

  record Response(
      Long userId,
      Long messageId,
      String nickname,
      String content,
      String profileImageUrl,
      LocalDateTime createdAt
  ) implements ChattingDto {

    public static Response of(User user, ChattingMessage chattingMessage) {
      return new Response(
          user.getId(),
          chattingMessage.getId(),
          user.getProfile().getNickname(),
          chattingMessage.getMessage(),
          user.getProfile().getProfileImageUrl(),
          chattingMessage.getCreatedAt()
      );
    }

  }

}
