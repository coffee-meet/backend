package coffeemeet.server.certification.service.cq;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.emailVerification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.certification.domain.EmailVerification;
import coffeemeet.server.certification.repository.EmailVerificationRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailVerificationQueryTest {

  @InjectMocks
  private EmailVerificationQuery emailVerificationQuery;
  @Mock
  private EmailVerificationRepository emailVerificationRepository;

  @Test
  void getCodeByIdTest() {
    // given
    EmailVerification emailVerification = emailVerification();

    given(emailVerificationRepository.findById(emailVerification.getUserId())).willReturn(
        Optional.of(emailVerification));

    // when
    String code = emailVerificationQuery.getCodeById(emailVerification.getUserId());

    // then
    assertThat(code).isEqualTo(emailVerification.getCode());
  }

}