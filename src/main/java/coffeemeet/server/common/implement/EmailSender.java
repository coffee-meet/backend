package coffeemeet.server.common.implement;

import coffeemeet.server.certification.domain.CompanyEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

  private final JavaMailSender javaMailSender;
  private final String sender;

  public EmailSender(JavaMailSender javaMailSender,
      @Value("${spring.mail.username}") String sender) {
    this.javaMailSender = javaMailSender;
    this.sender = sender;
  }

  public void sendVerificationCode(CompanyEmail companyMail, String verificationCode) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(sender);
    mailMessage.setTo(companyMail.getValue());

    String subject = "[coffee-meet] 커피밋 사용을 위해 이메일 인증을 완료해주세요";
    mailMessage.setSubject(subject);

    String text = String.format("인증코드: %s", verificationCode);
    mailMessage.setText(text);

    javaMailSender.send(mailMessage);
  }

}
