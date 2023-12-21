package coffeemeet.server.certification.implement;

import static coffeemeet.server.common.fixture.CertificationFixture.emailVerification;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.VerificationInfo;
import coffeemeet.server.certification.domain.repository.VerificationInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerificationInfoCommandTest {

  @InjectMocks
  private VerificationInfoCommand verificationInfoCommand;
  @Mock
  private VerificationInfoRepository verificationInfoRepository;

  @Test
  @DisplayName("새로운 VerificationInfo 객체를 저장할 수 있다.")
  void createEmailVerificationTest() {
    // given
    VerificationInfo verificationInfo = emailVerification();
    given(verificationInfoRepository.save(any(VerificationInfo.class))).willReturn(
        verificationInfo);

    // when
    verificationInfoCommand.createVerificationInfo(verificationInfo.getUserId(),
        verificationInfo.getCompanyEmail(), verificationInfo.getVerificationCode());

    // then
    then(verificationInfoRepository).should(only()).save(any(VerificationInfo.class));
  }

}
