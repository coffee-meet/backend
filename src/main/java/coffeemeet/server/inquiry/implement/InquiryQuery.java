package coffeemeet.server.inquiry.implement;

import static coffeemeet.server.inquiry.exception.InquiryErrorCode.NOT_EXIST_INQUIRY;

import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.infrastructure.InquiryQueryRepository;
import coffeemeet.server.inquiry.infrastructure.InquiryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryQuery {

  private static final String NOT_EXIST_INQUIRY_MESSAGE = "해당 아이디(%s)에 일치하는 문의는 존재하지 않습니다.";

  private final InquiryQueryRepository inquiryQueryRepository;
  private final InquiryRepository inquiryRepository;

  public List<Inquiry> getInquiriesBy(Long lastInquiryId, int pageSize) {
    return inquiryQueryRepository.findAllInquiries(lastInquiryId, pageSize);
  }

  public Inquiry getInquiryBy(Long inquiryId) {
    return inquiryRepository.findById(inquiryId)
        .orElseThrow(() -> new NotFoundException(
            NOT_EXIST_INQUIRY,
            String.format(NOT_EXIST_INQUIRY_MESSAGE, inquiryId))
        );
  }

}
