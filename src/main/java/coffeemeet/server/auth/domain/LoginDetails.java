package coffeemeet.server.auth.domain;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import java.util.List;

public record LoginDetails(
    String accessToken,
    String refreshToken,
    String nickname,
    String profileImageUrl,
    String companyName,
    Department department,
    List<Keyword> keywords
) {

  public static LoginDetails of(
      String accessToken,
      String refreshToken,
      String nickname,
      String profileImageUrl,
      String companyName,
      Department department,
      List<Keyword> keywords
  ) {
    return new LoginDetails(
        accessToken,
        refreshToken,
        nickname,
        profileImageUrl,
        companyName,
        department,
        keywords
    );
  }

}
