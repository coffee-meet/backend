package coffeemeet.server.user.service.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class LoginDetailsDto {

  public record Response(
      String accessToken,
      String refreshToken,
      String nickname,
      String profileImageUrl,
      String companyName,
      Department department,
      List<Keyword> interests
  ) {

    public static Response of(User user, List<Keyword> interests, Certification certification,
        AuthTokens authTokens) {
      return new Response(
          authTokens.accessToken(),
          authTokens.refreshToken(),
          user.getProfile().getNickname(),
          user.getProfile().getProfileImageUrl(),
          certification.getCompanyName(),
          certification.getDepartment(),
          interests
      );
    }

  }

}
