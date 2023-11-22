package coffeemeet.server.user.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.service.dto.LoginDetailsDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class LoginDetailsHTTP {

  public record Response(
      Long userId,
      boolean isRegistered,
      String accessToken,
      String refreshToken,
      String nickname,
      String profileImageUrl,
      String companyName,
      Department department,
      List<Keyword> interests
  ) {

    public static Response of(LoginDetailsDto.Response response) {
      return new Response(
          response.userId(),
          response.isRegistered(),
          response.accessToken(),
          response.refreshToken(),
          response.nickname(),
          response.profileImageUrl(),
          response.companyName(),
          response.department(),
          response.interests()
      );
    }
  }

}
