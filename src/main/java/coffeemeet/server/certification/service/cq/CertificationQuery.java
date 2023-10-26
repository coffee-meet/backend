package coffeemeet.server.certification.service.cq;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.repository.CertificationRepository;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationQuery {

  public static final String CERTIFICATION_NOT_FOUND = "해당 사용자의 인증정보를 찾을 수 없습니다.";
  private static final String EXISTED_COMPANY_EMAIL = "이미 사용 중인 회사 이메일입니다.";

  private final CertificationRepository certificationRepository;

  public Certification getCertificationByUserId(Long userId) {
    return certificationRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException(CERTIFICATION_NOT_FOUND));
  }

  public void checkDuplicatedCompanyEmail(CompanyEmail companyEmail) {
    if (certificationRepository.existsByCompanyEmail(companyEmail)) {
      throw new IllegalArgumentException(EXISTED_COMPANY_EMAIL);
    }
  }

  public void applyIfCertifiedUser(Long userId, Consumer<? super Certification> consumer) {
    certificationRepository.findByUserId(userId).ifPresent(consumer);
  }

}
