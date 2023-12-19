package coffeemeet.server.chatting.history.service.dto;

import coffeemeet.server.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

<<<<<<<< HEAD:src/main/java/coffeemeet/server/chatting/history/service/dto/ChattingHistory.java
public record ChattingHistory(
========
public record ChattingMessageHistory(
>>>>>>>> e82e9257 (refactor: 네이밍 변경):src/main/java/coffeemeet/server/chatting/history/service/dto/ChattingMessageHistory.java
    Long userId,
    Long messageId,
    String nickname,
    String content,
    String profileImageUrl,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdAt
) {

<<<<<<<< HEAD:src/main/java/coffeemeet/server/chatting/history/service/dto/ChattingHistory.java
  public static ChattingHistory of(User user,
      ChattingMessageHistory chattingMessageHistory) {
    return new ChattingHistory(
========
  public static ChattingMessageHistory of(User user,
      coffeemeet.server.chatting.history.domain.ChattingMessageHistory chattingMessageHistory) {
    return new ChattingMessageHistory(
>>>>>>>> e82e9257 (refactor: 네이밍 변경):src/main/java/coffeemeet/server/chatting/history/service/dto/ChattingMessageHistory.java
        user.getId(),
        chattingMessageHistory.getId(),
        user.getProfile().getNickname(),
        chattingMessageHistory.getMessage(),
        user.getOauthInfo().getProfileImageUrl(),
        chattingMessageHistory.getCreatedAt()
    );
  }

}
