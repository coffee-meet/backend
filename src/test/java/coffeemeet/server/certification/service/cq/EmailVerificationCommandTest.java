package coffeemeet.server.certification.service.cq;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.emailVerification;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.EmailVerification;
import coffeemeet.server.certification.implement.EmailVerificationCommand;
import coffeemeet.server.certification.infrastructure.EmailVerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailVerificationCommandTest {

  @InjectMocks
  private EmailVerificationCommand emailVerificationCommand;
  @Mock
  private EmailVerificationRepository emailVerificationRepository;

  @Test
  @DisplayName("새로운 EmailVerification 객체를 저장할 수 있다.")
  void createEmailVerificationTest() {
    // given
    EmailVerification emailVerification = emailVerification();
    given(emailVerificationRepository.save(any(EmailVerification.class))).willReturn(
        emailVerification);

    // when
    emailVerificationCommand.createEmailVerification(emailVerification.getUserId(),
        emailVerification.getCompanyEmail(), emailVerification.getCode());

    // then
    then(emailVerificationRepository).should(only()).save(any(EmailVerification.class));
  }

}
