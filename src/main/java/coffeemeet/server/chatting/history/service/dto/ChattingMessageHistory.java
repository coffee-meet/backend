package coffeemeet.server.chatting.history.service.dto;

import coffeemeet.server.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ChattingMessageHistory(
    Long userId,
    Long messageId,
    String nickname,
    String content,
    String profileImageUrl,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdAt
) {

  public static ChattingMessageHistory of(User user,
      coffeemeet.server.chatting.history.domain.ChattingMessageHistory chattingMessageHistory) {
    return new ChattingMessageHistory(
        user.getId(),
        chattingMessageHistory.getId(),
        user.getProfile().getNickname(),
        chattingMessageHistory.getMessage(),
        user.getOauthInfo().getProfileImageUrl(),
        chattingMessageHistory.getCreatedAt()
    );
  }

}
