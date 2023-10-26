package coffeemeet.server.certification.service.cq;

import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.EmailVerification;
import coffeemeet.server.certification.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationCommand {

  private final EmailVerificationRepository emailVerificationRepository;

  public void newEmailVerification(Long userId, CompanyEmail companyEmail,
      String verificationCode) {
    emailVerificationRepository.save(new EmailVerification(userId, companyEmail, verificationCode));
  }

}
