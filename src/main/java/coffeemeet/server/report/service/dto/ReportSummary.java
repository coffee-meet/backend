package coffeemeet.server.report.service.dto;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReportSummary(
    String targetedNickname,
    String chattingRoomName,
    Long targetedId,
    Long chattingRoomId,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdAt
) {

    public static ReportSummary of(User targeted, ChattingRoom chattingRoom) {
        return new ReportSummary(
            targeted.getProfile().getNickname(),
            chattingRoom.getName(),
            targeted.getId(),
            chattingRoom.getId(),
            targeted.getCreatedAt()
        );
    }

}
