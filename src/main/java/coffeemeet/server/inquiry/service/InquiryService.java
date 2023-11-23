package coffeemeet.server.inquiry.service;

import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.implement.InquiryCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {

  private final InquiryCommand inquiryCommand;

  public void registerInquiry(Long inquirerId, String title, String content) {
    inquiryCommand.createReport(new Inquiry(inquirerId, title, content));
  }

}
