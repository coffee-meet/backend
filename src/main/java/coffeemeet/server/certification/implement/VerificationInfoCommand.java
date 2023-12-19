package coffeemeet.server.certification.implement;

import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.VerificationInfo;
import coffeemeet.server.certification.domain.repository.VerificationInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationInfoCommand {

  private final VerificationInfoRepository verificationInfoRepository;

  public void createVerificationInfo(Long userId, CompanyEmail companyEmail,
      String verificationCode) {
    verificationInfoRepository.save(new VerificationInfo(userId, companyEmail, verificationCode));
  }

}
