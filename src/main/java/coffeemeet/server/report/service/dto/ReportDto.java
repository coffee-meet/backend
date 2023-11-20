package coffeemeet.server.report.service.dto;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public sealed interface ReportDto permits ReportDto.Response {

  record Response(
      String targetUserNickname,
      String chattingRoomName,
      LocalDateTime createdAt
  ) implements ReportDto {

    public static Response of(User targetUser, ChattingRoom chattingRoom) {
      return new Response(
          targetUser.getProfile().getNickname(),
          chattingRoom.getName(),
          targetUser.getCreatedAt()
      );
    }
  }

}
