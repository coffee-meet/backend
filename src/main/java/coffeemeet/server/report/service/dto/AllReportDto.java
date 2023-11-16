package coffeemeet.server.report.service.dto;

import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public sealed interface AllReportDto permits AllReportDto.Response {

  record Response(
      String targetUserNickname,
      int reportedCount,
      LocalDateTime createdAt
  ) implements AllReportDto {

    public static Response of(User targetUser) {
      return new Response(
          targetUser.getProfile().getNickname(),
          targetUser.getReportInfo().getReportedCount(),
          targetUser.getCreatedAt()
      );
    }
  }

}
