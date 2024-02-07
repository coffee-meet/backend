package coffeemeet.server.certification.implement;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.domain.repository.CertificationRepository;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CertificationCommand {

  private final CertificationRepository certificationRepository;
  private final CertificationQuery certificationQuery;
  private final UserQuery userQuery;

  public void createCertification(Long userId, String companyName, CompanyEmail companyEmail,
      Department department, String businessCardUrl) {
    User user = userQuery.getUserById(userId);
    certificationRepository.save(
        Certification.builder()
            .companyName(companyName)
            .companyEmail(companyEmail)
            .businessCardUrl(businessCardUrl)
            .department(department)
            .user(user)
            .build()
    );
  }

  public void updateCertification(Long userId, String companyName, CompanyEmail companyEmail,
      Department department, String businessCardImageUrl) {
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    certification.update(companyName, companyEmail, businessCardImageUrl, department);
  }

  public void completeCertification(Long userId) {
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    certification.qualify();
  }

  public void deleteCertificationByUserId(Long userId) {
    certificationRepository.deleteByUserId(userId);
  }

}
