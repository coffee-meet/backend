package coffeemeet.server.certification.implement;

import static coffeemeet.server.certification.exception.CertificationErrorCode.VERIFICATION_CODE_NOT_FOUND;

import coffeemeet.server.certification.domain.VerificationInfo;
import coffeemeet.server.certification.domain.repository.VerificationInfoRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationInfoQuery {

  private static final String VERIFICATION_CODE_NOT_FOUND_MESSAGE = "인증코드 기간이 만료되었거나 해당 유저(%s)가 인증코드를 요청한 기록이 없습니다.";

  private final VerificationInfoRepository verificationInfoRepository;

  public String getVerificationCodeById(Long userId) {
    VerificationInfo verificationInfo = verificationInfoRepository.findById(userId)
        .orElseThrow(() -> new InvalidInputException(
            VERIFICATION_CODE_NOT_FOUND,
            String.format(VERIFICATION_CODE_NOT_FOUND_MESSAGE, userId)));
    return verificationInfo.getVerificationCode();
  }

}
