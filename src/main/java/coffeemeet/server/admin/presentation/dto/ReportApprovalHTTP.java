package coffeemeet.server.admin.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ReportApprovalHTTP {

  public record Request(
      @NotNull
      Long reportId,
      @NotNull
      Long userId
  ) {

  }

}
