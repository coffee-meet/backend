package coffeemeet.server.certification.implement;

import static coffeemeet.server.common.fixture.CertificationFixture.companyEmail;
import static coffeemeet.server.common.fixture.CertificationFixture.verificationCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.common.infrastructure.EmailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerificationMailSenderTest {

  @InjectMocks
  private VerificationMailSender verificationMailSender;

  @Mock
  private EmailSender emailSender;


  @Test
  @DisplayName("인증 이메일 전송할 수 있다.")
  void sendVerificationMailTest() {
    // given
    CompanyEmail companyEmail = companyEmail();
    String verificationCode = verificationCode();

    // when
    verificationMailSender.sendVerificationMail(companyEmail, verificationCode);

    // then
    then(emailSender).should(only()).sendMail(any());
  }
}
