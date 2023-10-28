package coffeemeet.server.user.dto;

import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.domain.OAuthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public sealed interface SignupDto permits SignupDto.Request {

  record Request(
      @NotBlank
      String nickname,
      @NotNull
      List<Keyword> keywords,
      @NotBlank
      String authCode,
      OAuthProvider oAuthProvider
  ) implements SignupDto {

  }

}
