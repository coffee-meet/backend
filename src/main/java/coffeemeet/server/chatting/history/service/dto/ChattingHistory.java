package coffeemeet.server.chatting.history.service.dto;

import coffeemeet.server.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;

public record ChattingHistory(
    Long userId,
    Long messageId,
    String nickname,
    String content,
    String profileImageUrl,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdAt
) {

  public static ChattingHistory of(User user,
      coffeemeet.server.chatting.history.domain.ChattingMessageHistory chattingMessageHistory) {
    return new ChattingHistory(
        user.getId(),
        chattingMessageHistory.getId(),
        user.getProfile().getNickname(),
        chattingMessageHistory.getMessage(),
        user.getOauthInfo().getProfileImageUrl(),
        chattingMessageHistory.getCreatedAt()
    );
  }

}
