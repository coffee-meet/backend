package coffeemeet.server.common.infrastructure;

import coffeemeet.server.common.domain.CoffeeMeetMail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

  private final JavaMailSender javaMailSender;
  private final String sender;

  public EmailSender(JavaMailSender javaMailSender,
      @Value("${spring.mail.username}") String sender) {
    this.javaMailSender = javaMailSender;
    this.sender = sender;
  }

  @Async
  public void sendMail(CoffeeMeetMail coffeeMeetMail) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setFrom(sender);
    mailMessage.setTo(coffeeMeetMail.receiver());

    mailMessage.setSubject(coffeeMeetMail.title());
    mailMessage.setText(coffeeMeetMail.contents());

    javaMailSender.send(mailMessage); // TODO: 2023/12/19 에러 핸들링
  }

}
