package coffeemeet.server.user.controller.dto;

import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.domain.OAuthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.NonNull;

public sealed interface SignupHttpDto permits SignupHttpDto.Request {

  record Request(
      @NotBlank
      String nickname,
      @NotNull
      List<Keyword> keywords,
      @NotBlank
      String authCode,
      @NonNull
      OAuthProvider oAuthProvider
  ) implements SignupHttpDto {

  }

}
