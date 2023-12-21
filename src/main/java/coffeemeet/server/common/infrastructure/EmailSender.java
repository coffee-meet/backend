package coffeemeet.server.common.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

  public void sendEmail(String email, String subject, String body) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setFrom(sender);
    mailMessage.setTo(email);

    mailMessage.setSubject(subject);
    mailMessage.setText(body);

    javaMailSender.send(mailMessage); // TODO: 2023/12/19 에러 핸들링 및 비동기처리
  }

}
