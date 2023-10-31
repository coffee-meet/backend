package coffeemeet.server.certification.service.cq;

import static coffeemeet.server.certification.exception.CertificationErrorCode.CERTIFICATION_NOT_FOUND;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.repository.CertificationRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

}
