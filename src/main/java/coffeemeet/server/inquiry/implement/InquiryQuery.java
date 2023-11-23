package coffeemeet.server.inquiry.implement;

import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.infrastructure.InquiryQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryQuery {

  private final InquiryQueryRepository inquiryQueryRepository;

  public List<Inquiry> getInquiriesBy(Long lastInquiryId, int pageSize) {
    return inquiryQueryRepository.findAllInquiries(lastInquiryId, pageSize);
  }

}
