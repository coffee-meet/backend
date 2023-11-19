package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.domain.ReportReason;
import coffeemeet.server.report.service.dto.ReportDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Length;

public sealed interface ReportHTTP permits ReportHTTP.Request, ReportHTTP.Response {

  record Request(
      @NotNull
      Long chattingRoomId,
      @NotNull
      Long targetId,
      @NotBlank
      String reason,
      @NotBlank @Length(max = 200)
      String reasonDetail
  ) implements ReportHTTP {

  }

  record Response(
      String reporterNickname,
      String targetNickname,
      String targetEmail,
      ReportReason reason,
      String reasonDetail,
      int reportedCount,
      LocalDateTime createdAt
  ) implements ReportHTTP {

    public static ReportHTTP.Response of(ReportDto.Response response) {
      return new ReportHTTP.Response(
          response.reporterNickname(),
          response.targetNickname(),
          response.targetEmail(),
          response.reason(),
          response.reasonDetail(),
          response.reportedCount(),
          response.createAt()
      );
    }
  }

}