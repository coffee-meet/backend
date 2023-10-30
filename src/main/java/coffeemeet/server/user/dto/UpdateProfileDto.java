package coffeemeet.server.user.dto;

import coffeemeet.server.interest.domain.Keyword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public sealed interface UpdateProfileDto permits UpdateProfileDto.Request {

  record Request(
      @NotBlank String nickname,
      @NotNull @Size(min = 1, max = 3) List<Keyword> interests
  ) implements UpdateProfileDto {

  }

}
