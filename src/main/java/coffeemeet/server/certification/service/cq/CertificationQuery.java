package coffeemeet.server.certification.service.cq;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationQuery {

  private static final String CERTIFICATION_NOT_FOUND = "해당 사용자의 인증정보를 찾을 수 없습니다.";

  private final CertificationRepository certificationRepository;

  public Certification getCertificationByUserId(Long userId) {
    return certificationRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException(CERTIFICATION_NOT_FOUND));
  }

}
