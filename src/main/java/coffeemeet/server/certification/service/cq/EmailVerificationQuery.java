package coffeemeet.server.certification.service.cq;

import coffeemeet.server.certification.domain.EmailVerification;
import coffeemeet.server.certification.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationQuery {

  private static final String VERIFICATION_CODE_NOT_FOUND = "인증코드 기간이 만료되었거나 해당 유저가 인증 번호를 요청한 기록이 없습니다.";

  private final EmailVerificationRepository emailVerificationRepository;

  public String getCodeById(Long userId) {
    EmailVerification emailVerification = emailVerificationRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(VERIFICATION_CODE_NOT_FOUND));
    return emailVerification.getCode();
  }

}
