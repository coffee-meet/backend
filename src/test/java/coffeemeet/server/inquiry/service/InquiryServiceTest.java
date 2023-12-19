package coffeemeet.server.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import coffeemeet.server.common.fixture.InquiryFixture;
import coffeemeet.server.common.fixture.UserFixture;
import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.implement.InquiryCommand;
import coffeemeet.server.inquiry.implement.InquiryQuery;
import coffeemeet.server.inquiry.service.dto.InquiryDetailDto;
import coffeemeet.server.inquiry.service.dto.InquirySearchDto;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

  @Mock
  private InquiryQuery inquiryQuery;

  @Mock
  private UserQuery userQuery;

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

  @DisplayName("문의를 조회할 수 있다.")
  @Test
  void searchInquiriesTest() {
    // given
    Long lastInquiryId = 10L;
    int pageSize = 10;
    int size = 10;
    Long inquirerId = 1L;

    List<Inquiry> inquiries = InquiryFixture.inquiriesWithFixedId(size, inquirerId);
    Set<User> userSet = new HashSet<>(List.of(UserFixture.userWithFixedId(inquirerId)));
    given(inquiryQuery.getInquiriesBy(lastInquiryId, pageSize)).willReturn(inquiries);
    given(userQuery.getUsersByIdSet(anySet())).willReturn(userSet);

    // when
    InquirySearchDto inquirySearchDto = inquiryService.searchInquiries(lastInquiryId,
        pageSize);

    // then
    assertAll(
        () -> assertThat(inquirySearchDto.contents()).hasSize(size),
        () -> assertThat(inquirySearchDto.hasNext()).isTrue()
    );
  }

  @DisplayName("문의 아이디로 문의를 찾을 수 있다.")
  @Test
  void findInquiryByInquiryId() {
    // given
    Inquiry inquiry = InquiryFixture.inquiry();
    User user = UserFixture.user();

    given(inquiryQuery.getInquiryBy(inquiry.getId())).willReturn(inquiry);
    given(userQuery.getUserById(inquiry.getInquirerId())).willReturn(user);

    // when
    InquiryDetailDto response = inquiryService.findInquiryBy(inquiry.getId());

    // then
    assertThat(response.inquirerId()).isEqualTo(inquiry.getInquirerId());
  }

}
