package coffeemeet.server.matching.service;

import static coffeemeet.server.common.fixture.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.matching.implement.MatchingConditionChecker;
import coffeemeet.server.matching.implement.MatchingQueueAppender;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.matching.implement.MatchingValidator;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

  @InjectMocks
  private MatchingService matchingService;
  @Mock
  private CertificationQuery certificationQuery;
  @Mock
  private MatchingValidator matchingValidator;
  @Mock
  private UserQuery userQuery;
  @Mock
  private UserCommand userCommand;
  @Mock
  private MatchingQueueCommand matchingQueueCommand;
  @Mock
  private MatchingConditionChecker matchingConditionChecker;
  @Mock
  private MatchingQueueAppender matchingQueueAppender;

  @Test
  @DisplayName("매칭을 시작할 수 있다")
  void startMatchingTest() {
    // given
    Certification certification = certification();
    User user = certification.getUser();
    Long userId = user.getId();

    given(certificationQuery.getCertificationByUserId(userId)).willReturn(certification);

    // when
    matchingService.startMatching(userId);

    // then
    then(matchingValidator).should(only()).validateCertificatedUser(certification);
    then(matchingQueueAppender).should(only()).append(certification.getCompanyName(), userId);
    then(matchingConditionChecker).should(only()).check(certification.getCompanyName());
  }

  @Test
  @DisplayName("매칭을 취소할 수 있다")
  void cancelMatchingTest() {
    // given
    Certification certification = certification();
    User user = user();
    Long userId = user.getId();

    given(userQuery.getUserById(userId)).willReturn(user);


    // when
    matchingService.cancelMatching(userId);

    // then
    then(matchingValidator).should(only()).validateUserMatchingStatus(user);
    then(matchingQueueCommand).should(only()).deleteUserByUserId(userId);
    then(userCommand).should(only()).setToIdle(userId);
  }

}


