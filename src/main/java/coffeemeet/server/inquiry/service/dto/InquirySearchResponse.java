package coffeemeet.server.inquiry.service.dto;

import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.user.domain.Profile;
import java.util.List;

public record InquirySearchResponse(
    List<InquirySummary> contents,
    boolean hasNext) {

  public static InquirySearchResponse of(List<InquirySummary> inquiries, boolean hasNext) {
    return new InquirySearchResponse(
        inquiries,
        hasNext
    );

  }

  public record InquirySummary(
      Long inquiryId,
      String inquirer,
      String title
  ) {

    public static InquirySearchResponse.InquirySummary of(Inquiry inquiry, Profile profile) {
      return new InquirySearchResponse.InquirySummary(
          inquiry.getId(),
          profile.getNickname(),
          inquiry.getTitle()
      );
    }

  }

}
