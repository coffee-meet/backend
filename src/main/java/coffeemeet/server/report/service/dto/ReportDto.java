package coffeemeet.server.report.service.dto;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public final class ReportDto {

    public record Response(
            String targetedNickname,
            String chattingRoomName,
            LocalDateTime createdAt,
            boolean hasNext
    ) {

        public static Response of(User targeted, ChattingRoom chattingRoom, boolean hasNext) {
            return new Response(
                    targeted.getProfile().getNickname(),
                    chattingRoom.getName(),
                    targeted.getCreatedAt(),
                    hasNext
            );
        }
    }

}
