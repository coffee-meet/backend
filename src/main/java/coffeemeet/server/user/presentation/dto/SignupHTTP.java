package coffeemeet.server.user.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.user.domain.Keyword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = PRIVATE)
public final class SignupHTTP {

  public record Request(
      @NonNull
      Long userId,
      @NotBlank
      String nickname,
      @NotNull
      List<Keyword> keywords
  ) {

  }

}
