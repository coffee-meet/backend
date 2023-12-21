package coffeemeet.server.inquiry.service.dto;

import java.util.List;

public record InquirySearchDto(
    List<InquirySummary> contents,
    boolean hasNext) {

  public static InquirySearchDto of(List<InquirySummary> inquiries, boolean hasNext) {
    return new InquirySearchDto(
        inquiries,
        hasNext
    );
  }

}
