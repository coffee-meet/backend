package coffeemeet.server.common.implement;

import static coffeemeet.server.common.fixture.CertificationFixture.email;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.common.domain.CoffeeMeetMail;
import coffeemeet.server.common.infrastructure.EmailSender;
import java.util.Objects;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailSenderTest {

  private EmailSender emailSender;

  @Mock
  private JavaMailSender javaMailSender;

  private String sender;

  @Captor
  private ArgumentCaptor<SimpleMailMessage> simpleMailMessage;

  @BeforeEach
  void setUp() {
    sender = email();
    emailSender = new EmailSender(javaMailSender, sender);
  }

  @Test
  @DisplayName("메일을 보낼 수 있다.")
  void sendVerificationCodeTest() {
    // given
    CoffeeMeetMail coffeeMeetMail = new CoffeeMeetMail(email(), Instancio.create(String.class),
        Instancio.create(String.class));

    // when
    emailSender.sendMail(coffeeMeetMail);

    // then
    then(javaMailSender).should(only()).send(simpleMailMessage.capture());

    SimpleMailMessage sentMailMessage = simpleMailMessage.getValue();
    assertAll(
        () -> assertThat(sentMailMessage.getFrom()).isEqualTo(sender),
        () -> assertThat(Objects.requireNonNull(sentMailMessage.getTo())[0]).isEqualTo(
            coffeeMeetMail.receiver()),
        () -> assertThat(sentMailMessage.getSubject()).isEqualTo(
            coffeeMeetMail.title()),
        () -> assertThat(sentMailMessage.getText()).contains(coffeeMeetMail.contents())
    );
  }
}
