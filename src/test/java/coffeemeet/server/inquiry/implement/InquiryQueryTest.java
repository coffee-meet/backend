package coffeemeet.server.inquiry.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.fixture.entity.InquiryFixture;
import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.infrastructure.InquiryQueryRepository;
import coffeemeet.server.inquiry.infrastructure.InquiryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InquiryQueryTest {

  @InjectMocks
  private InquiryQuery inquiryQuery;

  @Mock
  private InquiryQueryRepository inquiryQueryRepository;

  @Mock
  private InquiryRepository inquiryRepository;

  @DisplayName("문의 내역을 가져올 수 있다.")
  @Test
  void getInquiriesByTest() {
    // given
    Long lastInquiryId = 10L;
    int pageSize = 10;
    List<Inquiry> inquiries = InquiryFixture.inquiries(10);

    given(inquiryQueryRepository.findAllInquiries(lastInquiryId, pageSize)).willReturn(inquiries);

    // when
    List<Inquiry> response = inquiryQuery.getInquiriesBy(lastInquiryId, pageSize);

    // then
    assertThat(response).isEqualTo(inquiries);
  }

  @DisplayName("문의 아이디로 문의를 가져올 수 있다.")
  @Test
  void getInquiryByInquiryIdTest() {
    // given
    Inquiry inquiry = InquiryFixture.inquiry();
    given(inquiryRepository.findById(anyLong())).willReturn(Optional.of(inquiry));

    // when
    Inquiry foundInquiry = inquiryQuery.getInquiryBy(inquiry.getInquirerId());

    // then
    assertThat(foundInquiry).isEqualTo(inquiry);
  }

}
