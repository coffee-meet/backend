package coffeemeet.server.chatting.current.service.dto;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ChattingDto(
    Long userId,
    Long messageId,
    String nickname,
    String content,
    String profileImageUrl,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdAt
) {

  public static ChattingDto of(User user, ChattingMessage chattingMessage) {
    return new ChattingDto(
        user.getId(),
        chattingMessage.getId(),
        user.getProfile().getNickname(),
        chattingMessage.getMessage(),
        user.getOauthInfo().getProfileImageUrl(),
        chattingMessage.getCreatedAt()
    );
  }

}
