package coffeemeet.server.inquiry.service.dto;

import java.util.List;

public record InquirySearchDto(
    List<InquirySummaryDto> contents,
    boolean hasNext) {

  public static InquirySearchDto of(List<InquirySummaryDto> inquiries, boolean hasNext) {
    return new InquirySearchDto(
        inquiries,
        hasNext
    );
  }

}
