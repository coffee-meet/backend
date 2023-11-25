package coffeemeet.server.user.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.service.dto.UserProfileDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class UserProfileHTTP {

  public record Response(
      String nickname,
      String profileImageUrl,
      Department department,
      List<Keyword> interests
  ) {

    public static Response of(UserProfileDto response) {
      return new Response(
          response.nickname(),
          response.profileImageUrl(),
          response.department(),
          response.interests()
      );
    }
  }

}
