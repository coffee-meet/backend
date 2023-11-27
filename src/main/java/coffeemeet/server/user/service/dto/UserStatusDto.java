package coffeemeet.server.user.service.dto;

import coffeemeet.server.user.domain.UserStatus;
import java.time.LocalDateTime;

public record UserStatusDto(
    UserStatus userStatus,
    LocalDateTime startedAt,
    Long chattingRoomId,
    Boolean isCertificated,
    LocalDateTime penaltyExpiration
) {

  public static UserStatusDto of(
      UserStatus userStatus,
      LocalDateTime startedAt,
      Long chattingRoomId,
      Boolean isCertificated,
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
