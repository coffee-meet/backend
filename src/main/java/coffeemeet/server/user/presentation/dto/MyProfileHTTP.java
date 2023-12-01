package coffeemeet.server.user.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.service.dto.MyProfileDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class MyProfileHTTP {

  public record Response(
      String nickname,
      String profileImageUrl,
      String companyName,
      Department department,
      List<Keyword> interests,
      OAuthProvider oAuthProvider
  ) {

    public static Response of(MyProfileDto response) {
      return new Response(
          response.nickname(),
          response.profileImageUrl(),
          response.companyName(),
          response.department(),
          response.interests(),
          response.oAuthProvider()
      );
    }
  }

}
