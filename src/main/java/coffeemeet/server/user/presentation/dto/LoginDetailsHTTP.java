package coffeemeet.server.user.presentation.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.service.dto.LoginDetailsDto;
import java.util.List;

public sealed interface LoginDetailsHTTP permits LoginDetailsHTTP.Response {

  record Response(
      String accessToken,
      String refreshToken,
      String nickname,
      String profileImageUrl,
      String companyName,
      Department department,
      List<Keyword> interests
  ) implements LoginDetailsHTTP {

    public static Response of(LoginDetailsDto.Response response) {
      return new Response(
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
