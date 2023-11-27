package coffeemeet.server.report.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.report.domain.ReportReason;
import coffeemeet.server.report.service.dto.ReportDetailDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ReportDetailHTTP {

  public record Response(
      String reporterNickname,
      String targetedNickname,
      String targetedEmail,
      ReportReason reason,
      String reasonDetail,
      int reportedCount,
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
      LocalDateTime createAt
  ) {

    public static Response from(ReportDetailDto response) {
      return new Response(
          response.reporterNickname(),
          response.targetedNickname(),
          response.targetedEmail(),
          response.reason(),
          response.reasonDetail(),
          response.reportedCount(),
          response.createAt()
      );
    }
  }

}
