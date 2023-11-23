package coffeemeet.server.inquiry.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;

import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.implement.InquiryCommand;
import coffeemeet.server.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

  @InjectMocks
  private InquiryService inquiryService;

  @Mock
  private InquiryCommand inquiryCommand;

  @DisplayName("문의를 등록할 수 있다.")
  @Test
  void registerInquiryTest() {
    // given
    User user = UserFixture.user();
    Long inquirerId = user.getId();

    willDoNothing().given(inquiryCommand).createReport(any(Inquiry.class));

    // when
    assertThatCode(
        () -> inquiryService.registerInquiry(inquirerId, "문의 제목",
            "문의 내용")).doesNotThrowAnyException();
  }

}
