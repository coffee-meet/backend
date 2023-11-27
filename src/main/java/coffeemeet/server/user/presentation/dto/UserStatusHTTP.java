package coffeemeet.server.user.presentation.dto;

import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.service.dto.UserStatusDto;
import java.time.LocalDateTime;

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
