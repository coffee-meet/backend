package coffeemeet.server.certification.implement;

import static coffeemeet.server.certification.exception.CertificationErrorCode.EXISTED_COMPANY_EMAIL;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.infrastructure.CertificationRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.user.domain.User;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class CertificationCommand {

  private static final String EXISTED_COMPANY_EMAIL_MESSAGE = "이미 사용 중인 회사 이메일(%s) 입니다.";

  private final CertificationRepository certificationRepository;
  private final CertificationQuery certificationQuery;

  public void createCertification(User user, String companyName, CompanyEmail companyEmail,
      Department department, String businessCardUrl) {
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

  public void hasDuplicatedCompanyEmail(CompanyEmail companyEmail) {
    if (certificationRepository.existsByCompanyEmail(companyEmail)) {
      throw new InvalidInputException(EXISTED_COMPANY_EMAIL,
          String.format(EXISTED_COMPANY_EMAIL_MESSAGE, companyEmail.getValue()));
    }
  }

  public void certificated(Long userId) {
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    certification.qualify();
  }

  @Transactional(readOnly = true)
  public void applyIfCertifiedUser(Long userId, Consumer<? super Certification> consumer) {
    certificationRepository.findByUserId(userId).ifPresent(consumer);
  }

  public void deleteUserCertification(Long userId) {
    certificationRepository.deleteById(userId);
  }

}
