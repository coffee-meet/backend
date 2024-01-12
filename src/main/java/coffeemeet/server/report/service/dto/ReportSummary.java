package coffeemeet.server.report.service.dto;

import coffeemeet.server.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReportSummary(
    String targetedNickname,
    String chattingRoomName,
    Long targetedId,
    Long chattingRoomId,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdAt
) {

  public static ReportSummary of(User targeted, Long chattingRoomId, String chattingRoomName) {
    return new ReportSummary(
        targeted.getProfile().getNickname(),
        chattingRoomName,
        targeted.getId(),
        chattingRoomId,
        targeted.getCreatedAt()
    );
  }

}
