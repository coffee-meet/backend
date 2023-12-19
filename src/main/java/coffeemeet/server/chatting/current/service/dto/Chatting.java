package coffeemeet.server.chatting.current.service.dto;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record Chatting(
    Long userId,
    Long messageId,
    String nickname,
    String content,
    String profileImageUrl,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdAt
) {

  public static Chatting of(User user, ChattingMessage chattingMessage) {
    return new Chatting(
        user.getId(),
        chattingMessage.getId(),
        user.getProfile().getNickname(),
        chattingMessage.getMessage(),
        user.getOauthInfo().getProfileImageUrl(),
        chattingMessage.getCreatedAt()
    );
  }

}
