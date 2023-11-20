package coffeemeet.server.report.service.dto;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public sealed interface ReportDto permits ReportDto.Response {

  record Response(
      String targetedNickname,
      String chattingRoomName,
      LocalDateTime createdAt
  ) implements ReportDto {

    public static Response of(User targeted, ChattingRoom chattingRoom) {
      return new Response(
          targeted.getProfile().getNickname(),
          chattingRoom.getName(),
          targeted.getCreatedAt()
      );
    }
  }

}
