package coffeemeet.server.certification.implement;

import static coffeemeet.server.certification.exception.CertificationErrorCode.CERTIFICATION_NOT_FOUND;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.repository.CertificationRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationQuery {

  private static final String CERTIFICATION_NOT_FOUND_MESSAGE = "해당 사용자(%s)의 인증정보를 찾을 수 없습니다.";

  private final CertificationRepository certificationRepository;

  public Certification getCertificationByUserId(Long userId) {
    return certificationRepository.findByUserId(userId)
        .orElseThrow(() -> new InvalidInputException(CERTIFICATION_NOT_FOUND,
            String.format(CERTIFICATION_NOT_FOUND_MESSAGE, userId)));
  }

  public String getCompanyNameByUserId(Long userId) {
    return certificationRepository.findByUserId(userId)
        .orElseThrow(() -> new InvalidInputException(CERTIFICATION_NOT_FOUND,
            String.format(CERTIFICATION_NOT_FOUND_MESSAGE, userId))
        ).getCompanyName();
  }

  public boolean isExistedCompanyEmail(CompanyEmail companyEmail) {
    return certificationRepository.existsByCompanyEmail(companyEmail);
  }

  public Page<Certification> getPendingCertification(Pageable pageable) {
    return certificationRepository.findPendingCertifications(pageable);
  }

}
