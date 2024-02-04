package coffeemeet.server.inquiry.implement;

import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.infrastructure.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InquiryCommand {

  private final InquiryQuery inquiryQuery;
  private final InquiryRepository inquiryRepository;

  public void createReport(Inquiry inquiry) {
    inquiryRepository.save(inquiry);
  }

  public void updateCheckedInquiry(Long inquiryId) {
    Inquiry inquiry = inquiryQuery.getInquiryBy(inquiryId);
    inquiry.checkInquiry();
  }

}
