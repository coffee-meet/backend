package coffeemeet.server.user.presentation.dto;

import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.OAuthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public sealed interface SignupHTTP permits SignupHTTP.Request {

  record Request(
      @NotBlank
      String nickname,
      @NotNull
      List<Keyword> keywords,
      @NotBlank
      String authCode,
      @NotNull
      OAuthProvider oAuthProvider
  ) implements SignupHTTP {

  }

}
