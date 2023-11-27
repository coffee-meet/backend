package coffeemeet.server.user.presentation.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.service.dto.UserStatusDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(NON_NULL)
public final class UserStatusHTTP {

  public record Response(
      UserStatus userStatus,
      LocalDateTime startedAt,
      Long chattingRoomId,
      Boolean isCertificated,
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
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
