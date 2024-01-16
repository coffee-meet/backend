package coffeemeet.server.certification.implement;

import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.common.domain.CoffeeMeetMail;
import coffeemeet.server.common.infrastructure.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationMailSender {

  private static final String VERIFICATION_MAIL_TITLE = "[coffee-meet] 커피밋 사용을 위해 이메일 인증을 완료해주세요";
  private static final String VERIFICATION_MAIL_CONTENTS_FORM = "인증코드: %s";

  private final EmailSender emailSender;

  public void sendVerificationMail(CompanyEmail companyEmail, String verificationCode) {
    emailSender.sendMail(createVerificationMail(companyEmail.getValue(), verificationCode));
  }

  private CoffeeMeetMail createVerificationMail(String receiver, String code) {
    String contents = String.format(VERIFICATION_MAIL_CONTENTS_FORM, code);
    return new CoffeeMeetMail(receiver, VERIFICATION_MAIL_TITLE, contents);
  }

}
