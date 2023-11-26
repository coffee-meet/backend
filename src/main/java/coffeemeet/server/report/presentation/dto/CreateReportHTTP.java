package coffeemeet.server.report.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor(access = PRIVATE)
public final class CreateReportHTTP {

  public record Request(
      @NotNull
      Long chattingRoomId,
      @NotNull
      Long targetedId,
      @NotBlank
      String reason,
      @NotBlank @Length(max = 200)
      String reasonDetail
  ) {

  }

}
