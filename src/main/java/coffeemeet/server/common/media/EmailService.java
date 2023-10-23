package coffeemeet.server.common.media;

import coffeemeet.server.user.domain.CompanyEmail;
import java.util.random.RandomGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private static final RandomGenerator randomGenerator = RandomGenerator.getDefault();
  private final JavaMailSender javaMailSender;
  private final String sender;

  public EmailService(JavaMailSender javaMailSender,
      @Value("${spring.mail.username}") String sender) {
    this.javaMailSender = javaMailSender;
    this.sender = sender;
  }

  public void sendVerificationMail(CompanyEmail companyMail, String verificationCode) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(sender);
    mailMessage.setTo(companyMail.getCompanyEmail());

    String subject = "[coffee-meet] 커피밋 사용을 위해 이메일 인증을 완료해주세요";
    mailMessage.setSubject(subject);

    String text = String.format("인증번호: %s", verificationCode);
    mailMessage.setText(text);

    javaMailSender.send(mailMessage);
  }

  public String generateVerificationCode() {
    return String.format("%06d", randomGenerator.nextInt(1000000));
  }

}
