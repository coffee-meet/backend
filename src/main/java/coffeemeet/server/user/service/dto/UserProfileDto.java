package coffeemeet.server.user.service.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class UserProfileDto {

  public record Response(
      String nickname,
      String profileImageUrl,
      Department department,
      List<Keyword> interests
  ) {

    public static Response of(User user, Department department,
        List<Keyword> interests) {
      return new Response(
          user.getProfile().getNickname(),
          user.getProfile().getProfileImageUrl(),
          department,
          interests
      );
    }
  }

}
