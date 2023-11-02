package coffeemeet.server.certification.implement;

import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.EmailVerification;
import coffeemeet.server.certification.infrastructure.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationCommand {

  private final EmailVerificationRepository emailVerificationRepository;

  public void createEmailVerification(Long userId, CompanyEmail companyEmail,
      String verificationCode) {
    emailVerificationRepository.save(new EmailVerification(userId, companyEmail, verificationCode));
  }

}
