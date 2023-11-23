package coffeemeet.server.admin.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ReportDeletionHTTP {

  public record Request(
      @NotNull
      Set<Long> reportIds
  ) {

  }

}
