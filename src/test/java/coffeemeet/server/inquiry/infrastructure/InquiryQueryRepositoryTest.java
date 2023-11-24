package coffeemeet.server.inquiry.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.common.fixture.entity.InquiryFixture;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class InquiryQueryRepositoryTest extends RepositoryTestConfig {

  @Autowired
  private InquiryRepository inquiryRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private InquiryQueryRepository inquiryQueryRepository;

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    inquiryRepository.deleteAll();
  }

  @DisplayName("문의 내역을 무한스크롤 방식으로 조회할 수 있다.")
  @Test
  void findAllInquiriesTest() {
    // given
    int size = 11;
    int pageSize = 10;
    Long lastInquiryId = 10L;

    User user = UserFixture.user();
    userRepository.save(user);
    List<Inquiry> inquiries = InquiryFixture.inquiries(size);
    inquiryRepository.saveAll(inquiries);

    // when
    List<Inquiry> response = inquiryQueryRepository.findAllInquiries(lastInquiryId, pageSize);

    // then
    int expectedSize = 1;
    assertThat(response).hasSize(expectedSize);
  }

}
