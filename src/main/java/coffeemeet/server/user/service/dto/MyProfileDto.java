package coffeemeet.server.user.service.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class MyProfileDto {

  public record Response(
      String nickname,
      String profileImageUrl,
      String companyName,
      Department department,
      List<Keyword> interests
  ) {

    public static Response of(User user, List<Keyword> interests, String companyName,
        Department department) {
      return new Response(
          user.getProfile().getNickname(),
          user.getProfile().getProfileImageUrl(),
          companyName,
          department,
          interests
      );
    }
  }

}
