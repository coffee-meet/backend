package coffeemeet.server.user.service.dto;

import java.time.LocalDateTime;
import coffeemeet.server.user.domain.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record UserStatusDto(
        UserStatus userStatus,
        LocalDateTime startedAt,
        Long chattingRoomId,
        boolean isCertificated,
        LocalDateTime penaltyExpiration
) {

    public static UserStatusDto of(
            UserStatus userStatus,
            LocalDateTime startedAt,
            Long chattingRoomId,
            boolean isCertificated,
            LocalDateTime penaltyExpiration
    ) {
        return new UserStatusDto(
                userStatus,
                startedAt,
                chattingRoomId,
                isCertificated,
                penaltyExpiration
        );
    }

}
