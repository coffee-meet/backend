package coffeemeet.server.report.service.dto;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record Report(
    String targetedNickname,
    String chattingRoomName,
    Long targetedId,
    Long chattingRoomId,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdAt
) {

  public static Report of(User targeted, ChattingRoom chattingRoom) {
    return new Report(
        targeted.getProfile().getNickname(),
        chattingRoom.getName(),
        targeted.getId(),
        chattingRoom.getId(),
        targeted.getCreatedAt()
    );
  }

}
