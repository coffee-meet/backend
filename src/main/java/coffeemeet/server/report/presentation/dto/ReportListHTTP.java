package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.ReportDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReportListHTTP {

  public record Response(ReportDto content) {

    public static Response from(ReportDto content) {
      return new Response(content);
    }
  }

}
