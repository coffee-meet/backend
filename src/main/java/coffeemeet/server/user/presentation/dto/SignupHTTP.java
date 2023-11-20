package coffeemeet.server.user.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.OAuthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.NonNull;
@NoArgsConstructor(access = PRIVATE)
public final class SignupHTTP {

  public record Request(
      @NotBlank
      String nickname,
      @NotNull
      List<Keyword> keywords,
      @NotBlank
      String authCode,
      @NonNull
      OAuthProvider oAuthProvider
  ) {

  }

}
