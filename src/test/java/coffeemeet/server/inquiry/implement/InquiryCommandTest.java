package coffeemeet.server.inquiry.implement;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.fixture.entity.InquiryFixture;
import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.infrastructure.InquiryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InquiryCommandTest {

  @InjectMocks
  private InquiryCommand inquiryCommand;

  @Mock
  private InquiryRepository inquiryRepository;

  @DisplayName("문의를 만들 수 있다.")
  @Test
  void createReportTest() {
    // given
    Inquiry inquiry = InquiryFixture.inquiry();

    given(inquiryRepository.save(any(Inquiry.class))).willReturn(inquiry);

    // when, then
    assertThatCode(() -> inquiryCommand.createReport(inquiry)).doesNotThrowAnyException();
  }

}
