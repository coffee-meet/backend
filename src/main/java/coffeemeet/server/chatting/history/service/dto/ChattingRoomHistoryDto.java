package coffeemeet.server.chatting.history.service.dto;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ChattingRoomHistoryDto(
    Long roomId,
    String roomName,
    List<String> users,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdAt
) {

  public static ChattingRoomHistoryDto of(List<User> users,
      ChattingRoomHistory chattingRoomHistory) {
    List<String> userNicknames = users.stream()
        .map(user -> user.getProfile().getNickname())
        .toList();
    return new ChattingRoomHistoryDto(
        chattingRoomHistory.getId(),
        chattingRoomHistory.getName(),
        userNicknames,
        chattingRoomHistory.getCreatedAt()
    );
  }

}
