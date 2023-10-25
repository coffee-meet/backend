package coffeemeet.server.user.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

public record MyProfileResponse(
    String name,
    String nickname,
    String email,
    String profileImageUrl,
    String birthYear,
    String birthDay,
    int reportedCount,
    LocalDateTime sanctionPeriod,
    Department department,
    List<Keyword> interests
) {

  public static MyProfileResponse of(User user, List<Interest> interests, Department department) {
    List<Keyword> keywords = interests.stream()
        .map(Interest::getKeyword)
        .toList();

    return new MyProfileResponse(
        user.getProfile().getName(),
        user.getProfile().getNickname(),
        user.getProfile().getEmail().getEmail(),
        user.getProfile().getProfileImageUrl(),
        user.getProfile().getBirth().getBirthYear(),
        user.getProfile().getBirth().getBirthDay(),
        user.getReportInfo().getReportedCount(),
        user.getReportInfo().getSanctionPeriod(),
        department,
        keywords
    );
  }

}
