package coffeemeet.server.user.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.user.domain.Keyword;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class UpdateProfileHTTP {

  public record Request(
      String nickname,
      @Size(min = 1, max = 3) List<Keyword> interests
  ) {

  }

}
