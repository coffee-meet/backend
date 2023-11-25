package coffeemeet.server.user.presentation.dto;

import java.time.LocalDateTime;
import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.service.dto.UserStatusDto;

public final class UserStatusHTTP {

    public record Response(
            UserStatus userStatus,
            LocalDateTime startedAt,
            Long chattingRoomId,
            boolean isCertificated,
            LocalDateTime penaltyExpiration
    ) {

        public static Response of(UserStatusDto response) {
            return new Response(
                    response.userStatus(),
                    response.startedAt(),
                    response.chattingRoomId(),
                    response.isCertificated(),
                    response.penaltyExpiration()
            );
        }
    }

}
