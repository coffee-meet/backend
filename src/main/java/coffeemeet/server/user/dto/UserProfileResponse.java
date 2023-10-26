package coffeemeet.server.user.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.util.List;

public record UserProfileResponse(
    String nickname,
    String profileImageUrl,
    Department department,
    List<Keyword> interests
) {

  public static UserProfileResponse of(User user, Department department,
      List<Interest> interests) {
    List<Keyword> keywords = interests.stream()
        .map(Interest::getKeyword)
        .toList();

    return new UserProfileResponse(
        user.getProfile().getNickname(),
        user.getProfile().getProfileImageUrl(),
        department,
        keywords
    );
  }

}
