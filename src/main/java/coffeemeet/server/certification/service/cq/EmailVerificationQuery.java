package coffeemeet.server.certification.service.cq;

import static coffeemeet.server.certification.exception.CertificationErrorCode.VERIFICATION_CODE_NOT_FOUND;

import coffeemeet.server.certification.domain.EmailVerification;
import coffeemeet.server.certification.repository.EmailVerificationRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationQuery {

  private static final String VERIFICATION_CODE_NOT_FOUND_MESSAGE = "인증코드 기간이 만료되었거나 해당 유저(%s)가 인증 번호를 요청한 기록이 없습니다.";

  private final EmailVerificationRepository emailVerificationRepository;

  public String getCodeById(Long userId) {
    EmailVerification emailVerification = emailVerificationRepository.findById(userId)
        .orElseThrow(() -> new InvalidInputException(
            VERIFICATION_CODE_NOT_FOUND,
            String.format(VERIFICATION_CODE_NOT_FOUND_MESSAGE, userId)));
    return emailVerification.getCode();
  }

}
