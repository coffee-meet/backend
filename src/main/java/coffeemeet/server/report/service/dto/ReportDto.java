package coffeemeet.server.report.service.dto;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public record ReportDto(
        String targetedNickname,
        String chattingRoomName,
        LocalDateTime createdAt
) {

    public static ReportDto of(User targeted, ChattingRoom chattingRoom) {
        return new ReportDto(
                targeted.getProfile().getNickname(),
                chattingRoom.getName(),
                targeted.getCreatedAt()
        );
    }

}
