package coffeemeet.server.user.presentation.dto;

import coffeemeet.server.user.domain.Keyword;
import jakarta.validation.constraints.Size;
import java.util.List;

public sealed interface UpdateProfileHttpDto permits UpdateProfileHttpDto.Request {

  record Request(
      String nickname,
      @Size(min = 1, max = 3) List<Keyword> interests
  ) implements UpdateProfileHttpDto {

  }

}
