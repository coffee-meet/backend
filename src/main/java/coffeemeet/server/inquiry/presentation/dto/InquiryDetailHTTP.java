package coffeemeet.server.inquiry.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.inquiry.service.dto.InquiryDetailDto;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class InquiryDetailHTTP {

  public record Response(
      Long inquirerId,
      String inquirerNickname,
      String inquirerEmail,
      String title,
      String content,
      LocalDateTime createAt
  ) {

    public static InquiryDetailHTTP.Response from(InquiryDetailDto response) {
      return new InquiryDetailHTTP.Response(
          response.inquirerId(),
          response.inquirerNickname(),
          response.inquirerEmail(),
          response.title(),
          response.content(),
          response.createAt()
      );
    }
  }

}
