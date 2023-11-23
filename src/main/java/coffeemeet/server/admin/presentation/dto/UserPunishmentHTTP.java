package coffeemeet.server.admin.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class UserPunishmentHTTP {

  public record Request(
      @NotEmpty
      Set<Long> reportIds
  ) {

  }

}
