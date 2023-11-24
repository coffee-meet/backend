package coffeemeet.server.inquiry.implement;

import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.infrastructure.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class InquiryCommand {

  private final InquiryRepository inquiryRepository;

  public void createReport(Inquiry inquiry) {
    inquiryRepository.save(inquiry);
  }

}
