package coffeemeet.server.certification.service.cq;

import static coffeemeet.server.certification.exception.CertificationErrorCode.EXISTED_COMPANY_EMAIL;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.repository.CertificationRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.user.domain.User;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CertificationCommand {

  private static final String EXISTED_COMPANY_EMAIL_MESSAGE = "이미 사용 중인 회사 이메일(%s) 입니다.";

  private final CertificationRepository certificationRepository;

  public void newCertification(CompanyEmail companyEmail, String businessCardUrl,
      Department department, User user) {
    certificationRepository.save(
        Certification.builder()
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

  public void applyIfCertifiedUser(Long userId, Consumer<? super Certification> consumer) {
    certificationRepository.findByUserId(userId).ifPresent(consumer);
  }

}
