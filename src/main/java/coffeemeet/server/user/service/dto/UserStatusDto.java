package coffeemeet.server.user.service.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import coffeemeet.server.user.domain.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(NON_NULL)
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
