package coffeemeet.server.matching.implement;

import static coffeemeet.server.matching.exception.MatchingErrorCode.INVALID_USER_STATUS;
import static coffeemeet.server.matching.exception.MatchingErrorCode.NOT_CERTIFICATED_USER;
import static coffeemeet.server.user.domain.UserStatus.MATCHING;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.common.execption.BadRequestException;
import coffeemeet.server.common.execption.ForbiddenException;
import coffeemeet.server.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class MatchingValidator {

  private static final String NOT_CERTIFICATED_USER_MESSAGE = "사용자(%s) 인증이 완료되지 않았습니다.";
  private static final String NOT_MATCHING_STATUS_USER_MESSAGE = "유저 상태가 매칭 상태가 아닙니다.";

  public void validateCertificatedUser(Certification certification) {
    if (!certification.isCertificated()) {
      throw new ForbiddenException(NOT_CERTIFICATED_USER,
          String.format(NOT_CERTIFICATED_USER_MESSAGE, certification.getId()));
    }
  }

  public void validateUserMatchingStatus(User user) {
    if (user.getUserStatus() != MATCHING) {
      throw new BadRequestException(INVALID_USER_STATUS,
          NOT_MATCHING_STATUS_USER_MESSAGE);
    }
  }

}
