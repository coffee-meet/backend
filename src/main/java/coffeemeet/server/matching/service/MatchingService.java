package coffeemeet.server.matching.service;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.matching.implement.MatchingConditionChecker;
import coffeemeet.server.matching.implement.MatchingQueueAppender;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.matching.implement.MatchingValidator;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingService {

  private final CertificationQuery certificationQuery;
  private final MatchingValidator matchingValidator;
  private final UserQuery userQuery;
  private final UserCommand userCommand;
  private final MatchingQueueCommand matchingQueueCommand;
  private final MatchingConditionChecker matchingConditionChecker;
  private final MatchingQueueAppender matchingQueueAppender;

  public void startMatching(Long userId) {
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    matchingValidator.validateCertificatedUser(certification);

    String companyName = certification.getCompanyName();
    matchingQueueAppender.append(companyName, userId);
    matchingConditionChecker.check(companyName);
  }

  @Transactional
  public void cancelMatching(Long userId) {
    User user = userQuery.getUserById(userId);
    matchingValidator.validateUserMatchingStatus(user);
    matchingQueueCommand.deleteUserByUserId(userId);
    userCommand.setToIdle(userId);
  }

}
