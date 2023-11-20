package coffeemeet.server.chatting.history.service.dto;

import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public sealed interface ChattingMessageHistoryDto permits ChattingMessageHistoryDto.Response {

  record Response(
      Long userId,
      Long messageId,
      String nickname,
      String content,
      String profileImageUrl,
      LocalDateTime createdAt
  ) implements ChattingMessageHistoryDto {

    public static Response of(User user, ChattingMessageHistory chattingMessageHistory) {
      return new Response(
          user.getId(),
          chattingMessageHistory.getId(),
          user.getProfile().getNickname(),
          chattingMessageHistory.getMessage(),
          user.getProfile().getProfileImageUrl(),
          chattingMessageHistory.getCreatedAt()
      );
    }

  }

}
