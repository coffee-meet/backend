package coffeemeet.server.certification.implement;

import static coffeemeet.server.certification.exception.CertificationErrorCode.VERIFICATION_CODE_NOT_FOUND;

import coffeemeet.server.certification.domain.EmailVerification;
import coffeemeet.server.certification.infrastructure.EmailVerificationRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationQuery {

  private static final String VERIFICATION_CODE_NOT_FOUND_MESSAGE = "인증코드 기간이 만료되었거나 해당 유저(%s)가 인증코드를 요청한 기록이 없습니다.";

  private final EmailVerificationRepository emailVerificationRepository;

  public String getCodeById(Long userId) {
    EmailVerification emailVerification = emailVerificationRepository.findById(userId)
        .orElseThrow(() -> new InvalidInputException(
            VERIFICATION_CODE_NOT_FOUND,
            String.format(VERIFICATION_CODE_NOT_FOUND_MESSAGE, userId)));
    return emailVerification.getCode();
  }

}
