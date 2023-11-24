package coffeemeet.server.inquiry.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor(access = PRIVATE)
public final class InquiryHTTP {

  public record Request(
      @NotBlank @Length(max = 20) String title,
      @NotBlank @Length(max = 200) String content
  ) {

  }

}
