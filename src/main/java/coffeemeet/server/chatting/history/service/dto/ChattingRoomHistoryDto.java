package coffeemeet.server.chatting.history.service.dto;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface ChattingRoomHistoryDto permits ChattingRoomHistoryDto.Response {

  record Response(
      Long roomId,
      String roomName,
      List<String> users,
      LocalDateTime createdAt
  ) implements ChattingRoomHistoryDto {

    public static Response of(List<User> users, ChattingRoomHistory chattingRoomHistory) {
      List<String> userNicknames = users.stream()
          .map(user -> user.getProfile().getNickname())
          .toList();
      return new Response(
          chattingRoomHistory.getId(),
          chattingRoomHistory.getName(),
          userNicknames,
          chattingRoomHistory.getCreatedAt()
      );
    }
  }

}
