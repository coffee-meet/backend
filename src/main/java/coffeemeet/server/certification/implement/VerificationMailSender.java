package coffeemeet.server.certification.implement;

import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.common.infrastructure.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationMailSender {

  private static final String VERIFICATION_MAIL_SUBJECT = "[coffee-meet] 커피밋 사용을 위해 이메일 인증을 완료해주세요";
  private static final String VERIFICATION_MAIL_BODY = "인증코드: %s";

  private final EmailSender emailSender;

  public void sendVerificationMail(CompanyEmail companyEmail, String verificationCode) {
    emailSender.sendEmail(companyEmail.getValue(), VERIFICATION_MAIL_SUBJECT,
        String.format(VERIFICATION_MAIL_BODY, verificationCode));
  }

}
